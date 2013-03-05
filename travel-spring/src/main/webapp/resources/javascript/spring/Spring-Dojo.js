/*
 * Copyright 2004-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
dojo.declare("Spring.DefaultEquals", null, {equals: function (_1) {
    if (_1.declaredClass && _1.declaredClass == this.declaredClass) {
        return true;
    } else {
        return false;
    }
}});
dojo.declare("Spring.ElementDecoration", [Spring.AbstractElementDecoration, Spring.DefaultEquals], {constructor: function (_2) {
    this.copyFields = new Array("name", "value", "type", "checked", "selected", "readOnly", "disabled", "alt", "maxLength", "class", "title");
    dojo.mixin(this, _2);
    this.element = dojo.byId(this.elementId);
    this.elementId = dojo.isString(this.elementId) ? this.elementId : this.elementId.id;
    if (this.widgetModule == "") {
        this.widgetModule = this.widgetType;
    }
}, apply: function () {
    if (dijit.byId(this.elementId)) {
        dijit.byId(this.elementId).destroyRecursive(false);
    }
    if (!this.element) {
        console.error("Could not apply " + this.widgetType + " decoration.  Element with id '" + this.elementId + "' not found in the DOM.");
    } else {
        var _3 = this.widgetAttrs["datePattern"];
        if (_3 && this.widgetType == "dijit.form.DateTextBox") {
            if (!this.widgetAttrs["value"]) {
                this.widgetAttrs["value"] = dojo.date.locale.parse(this.element.value, {selector: "date", datePattern: _3});
            }
            if (!this.widgetAttrs["serialize"]) {
                this.widgetAttrs["serialize"] = function (d, _5) {
                    return dojo.date.locale.format(d, {selector: "date", datePattern: _3});
                };
            }
        }
        for (var _6 in this.copyFields) {
            _6 = this.copyFields[_6];
            if (!this.widgetAttrs[_6] && this.element[_6] && (typeof this.element[_6] != "number" || (typeof this.element[_6] == "number" && this.element[_6] >= 0))) {
                this.widgetAttrs[_6] = this.element[_6];
            }
        }
        if (this.element["style"] && this.element["style"].cssText) {
            this.widgetAttrs["style"] = this.element["style"].cssText;
        }
        dojo.require(this.widgetModule);
        var _7 = dojo.eval(this.widgetType);
        this.widget = new _7(this.widgetAttrs, this.element);
        this.widget.startup();
    }
    return this;
}, validate: function () {
    if (!this.widget.isValid) {
        return true;
    }
    var _8 = this.widget.isValid(false);
    if (!_8) {
        this.widget.state = "Error";
        this.widget._setStateClass();
    }
    return _8;
}});
dojo.declare("Spring.ValidateAllDecoration", [Spring.AbstractValidateAllDecoration, Spring.DefaultEquals], {constructor: function (_9) {
    this.originalHandler = null;
    this.connection = null;
    dojo.mixin(this, _9);
}, apply: function () {
    var _a = dojo.byId(this.elementId);
    if (!_a) {
        console.error("Could not apply ValidateAll decoration.  Element with id '" + this.elementId + "' not found in the DOM.");
    } else {
        this.originalHandler = _a[this.event];
        var _b = this;
        _a[this.event] = function (_c) {
            _b.handleEvent(_c, _b);
        };
    }
    return this;
}, cleanup: function () {
    dojo.disconnect(this.connection);
}, handleEvent: function (_d, _e) {
    if (!Spring.validateAll()) {
        dojo.publish(this.elementId + "/validation", [false]);
        dojo.stopEvent(_d);
    } else {
        dojo.publish(this.elementId + "/validation", [true]);
        if (dojo.isFunction(_e.originalHandler)) {
            var _f = _e.originalHandler(_d);
            if (_f == false) {
                dojo.stopEvent(_d);
            }
        }
    }
}});
dojo.declare("Spring.AjaxEventDecoration", [Spring.AbstractAjaxEventDecoration, Spring.DefaultEquals], {constructor: function (_10) {
    this.validationSubscription = null;
    this.connection = null;
    this.allowed = true;
    dojo.mixin(this, _10);
}, apply: function () {
    var _11 = dijit.byId(this.elementId) ? dijit.byId(this.elementId) : dojo.byId(this.elementId);
    if (!_11) {
        console.error("Could not apply AjaxEvent decoration.  Element with id '" + this.elementId + "' not found in the DOM.");
    } else {
        this.validationSubscription = dojo.subscribe(this.elementId + "/validation", this, "_handleValidation");
        this.connection = dojo.connect(_11, this.event, this, "submit");
    }
    return this;
}, cleanup: function () {
    dojo.unsubscribe(this.validationSubscription);
    dojo.disconnect(this.connection);
}, submit: function (_12) {
    if (this.sourceId == "") {
        this.sourceId = this.elementId;
    }
    if (this.formId == "") {
        Spring.remoting.getLinkedResource(this.sourceId, this.params, this.popup);
    } else {
        if (this.allowed) {
            Spring.remoting.submitForm(this.sourceId, this.formId, this.params);
        }
    }
    dojo.stopEvent(_12);
}, _handleValidation: function (_13) {
    if (!_13) {
        this.allowed = false;
    } else {
        this.allowed = true;
    }
}});
dojo.declare("Spring.RemotingHandler", Spring.AbstractRemotingHandler, {constructor: function () {
}, submitForm: function (_14, _15, _16) {
    var _17 = new Object();
    for (var key in _16) {
        _17[key] = _16[key];
    }
    var _19 = dojo.byId(_14);
    if (_19 != null) {
        if (_19.value != undefined && _19.type && ("button,submit,reset").indexOf(_19.type) < 0) {
            _17[_14] = _19.value;
        } else {
            if (_19.name != undefined) {
                _17[_19.name] = _19.name;
            } else {
                _17[_14] = _14;
            }
        }
    }
    if (!_17["ajaxSource"]) {
        _17["ajaxSource"] = _14;
    }
    var _1a = dojo.byId(_15);
    var _1b = dojo.string.trim(_1a.method);
    _1b = _1b.length > 0 ? _1b.toUpperCase() : "GET";
    dojo.xhr(_1b, {content: _17, form: _15, handleAs: "text", headers: {"Accept": "text/html;type=ajax"}, load: this.handleResponse, error: this.handleError}, _1b == "POST" ? true : false);
}, getLinkedResource: function (_1c, _1d, _1e) {
    this.getResource(dojo.byId(_1c).href, _1d, _1e);
}, getResource: function (_1f, _20, _21) {
    dojo.xhrGet({url: _1f, content: _20, handleAs: "text", headers: {"Accept": "text/html;type=ajax"}, load: this.handleResponse, error: this.handleError, modal: _21});
}, handleResponse: function (_22, _23) {
    var _24 = _23.xhr.getResponseHeader("Spring-Redirect-URL");
    var _25 = _23.xhr.getResponseHeader("Spring-Modal-View");
    var _26 = ((dojo.isString(_25) && _25.length > 0) || _23.args.modal);
    if (dojo.isString(_24) && _24.length > 0) {
        if (_26) {
            Spring.remoting._renderURLToModalDialog(_24);
            return _22;
        } else {
            if (_24.indexOf("/") >= 0) {
                window.location = window.location.protocol + "//" + window.location.host + _24;
            } else {
                var _27 = window.location.protocol + "//" + window.location.host + window.location.pathname;
                var _28 = _27.lastIndexOf("/");
                _27 = _27.substr(0, _28 + 1) + _24;
                if (_27 == window.location) {
                    Spring.remoting.getResource(_27, _23.args.content, false);
                } else {
                    window.location = _27;
                }
            }
            return _22;
        }
    }
    var _29 = "(?:<script(.|[\n|\r])*?>)((\n|\r|.)*?)(?:</script>)";
    var _2a = [];
    var _2b = new RegExp(_29, "img");
    var _2c = new RegExp(_29, "im");
    var _2d = _22.match(_2b);
    if (_2d != null) {
        for (var i = 0; i < _2d.length; i++) {
            var _2f = (_2d[i].match(_2c) || ["", "", ""])[2];
            _2f = _2f.replace(/<!--/mg, "").replace(/\/\/-->/mg, "").replace(/<!\[CDATA\[(\/\/>)*/mg, "").replace(/(<!)*\]\]>/mg, "");
            _2a.push(_2f);
        }
    }
    _22 = _22.replace(_2b, "");
    if (_26) {
        Spring.remoting._renderResponseToModalDialog(_22);
    } else {
        var _30 = dojo.doc.createElement("span");
        _30.id = "ajaxResponse";
        _30.style.display = "none";
        document.body.appendChild(_30);
        _30.innerHTML = _22;
        var _31 = new dojo.NodeList(_30);
        var _32 = _31.query("#ajaxResponse > *").orphan();
        _31.orphan();
        _32.forEach(function (_33) {
            if (_33.id != null && _33.id != "") {
                var _34 = dijit.byId(_33.id) ? dijit.byId(_33.id).domNode : dojo.byId(_33.id);
                if (!_34) {
                    console.error("An existing DOM elment with id '" + _33.id + "' could not be found for replacement.");
                } else {
                    _34.parentNode.replaceChild(_33, _34);
                }
            }
        });
    }
    dojo.forEach(_2a, function (_35) {
        dojo.eval(_35);
    });
    return _22;
}, handleError: function (_36, _37) {
    dojo.require("dijit.Dialog");
    console.error("HTTP status code: ", _37.xhr.status);
    if (Spring.debug && _37.xhr.status != 200) {
        var _38 = new dijit.Dialog({});
        dojo.connect(_38, "hide", _38, function () {
            this.destroyRecursive(false);
        });
        _38.domNode.style.width = "80%";
        _38.domNode.style.height = "80%";
        _38.domNode.style.textAlign = "left";
        _38.setContent(_37.xhr.responseText);
        _38.show();
    }
    return _36;
}, _renderURLToModalDialog: function (url) {
    Spring.remoting.getResource(url, {}, true);
}, _renderResponseToModalDialog: function (_3a) {
    dojo.require("dijit.Dialog");
    var _3b = new dijit.Dialog({});
    _3b.setContent(_3a);
    dojo.connect(_3b, "hide", _3b, function () {
        this.destroyRecursive(false);
    });
    _3b.show();
}});
dojo.declare("Spring.CommandLinkDecoration", [Spring.AbstractCommandLinkDecoration, Spring.DefaultEquals], {constructor: function (_3c) {
    dojo.mixin(this, _3c);
}, apply: function () {
    var _3d = dojo.byId(this.elementId);
    if (!dojo.hasClass(_3d, "progressiveLink")) {
        var _3e = new dojo.NodeList(_3d);
        _3e.addContent(this.linkHtml, "after").orphan("*");
        _3d = dojo.byId(this.elementId);
    }
    _3d.submitFormFromLink = this.submitFormFromLink;
    return this;
}, submitFormFromLink: function (_3f, _40, _41) {
    var _42 = [];
    var _43 = dojo.byId(_3f);
    var _44 = document.createElement("input");
    _44.name = _40;
    _44.value = "submitted";
    _42.push(_44);
    dojo.forEach(_41, function (_45) {
        var _46 = document.createElement("input");
        _46.name = _45.name;
        _46.value = _45.value;
        _42.push(_46);
    });
    dojo.forEach(_42, function (_47) {
        dojo.addClass(_47, "SpringLinkInput");
        dojo.place(_47, _43, "last");
    });
    if ((_43.onsubmit ? !_43.onsubmit() : false) || !_43.submit()) {
        dojo.forEach(_42, function (_48) {
            _48.parentNode.removeChild(_48);
        });
    }
}});
dojo.addOnLoad(Spring.initialize);