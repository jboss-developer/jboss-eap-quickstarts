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
Spring = {};
Spring.debug = true;
Spring.decorations = {};
Spring.decorations.applied = false;
Spring.initialize = function () {
    Spring.applyDecorations();
    Spring.remoting = new Spring.RemotingHandler();
};
Spring.addDecoration = function (_1) {
    if (!Spring.decorations[_1.elementId]) {
        Spring.decorations[_1.elementId] = [];
        Spring.decorations[_1.elementId].push(_1);
    } else {
        var _2 = false;
        for (var i = 0; i < Spring.decorations[_1.elementId].length; i++) {
            var _4 = Spring.decorations[_1.elementId][i];
            if (_4.equals(_1)) {
                if (_4.cleanup != undefined) {
                    _4.cleanup();
                }
                Spring.decorations[_1.elementId][i] = _1;
                _2 = true;
                break;
            }
        }
        if (!_2) {
            Spring.decorations[_1.elementId].push(_1);
        }
    }
    if (Spring.decorations.applied) {
        _1.apply();
    }
};
Spring.applyDecorations = function () {
    if (!Spring.decorations.applied) {
        for (var _5 in Spring.decorations) {
            for (var x = 0; x < Spring.decorations[_5].length; x++) {
                Spring.decorations[_5][x].apply();
            }
        }
        Spring.decorations.applied = true;
    }
};
Spring.validateAll = function () {
    var _7 = true;
    for (var _8 in Spring.decorations) {
        for (var x = 0; x < Spring.decorations[_8].length; x++) {
            if (Spring.decorations[_8][x].widget && !Spring.decorations[_8][x].validate()) {
                _7 = false;
            }
        }
    }
    return _7;
};
Spring.validateRequired = function () {
    var _a = true;
    for (var _b in Spring.decorations) {
        for (var x = 0; x < Spring.decorations[_b].length; x++) {
            if (Spring.decorations[_b][x].widget && Spring.decorations[_b][x].isRequired() && !Spring.decorations[_b][x].validate()) {
                _a = false;
            }
        }
    }
    return _a;
};
Spring.AbstractElementDecoration = function () {
};
Spring.AbstractElementDecoration.prototype = {elementId: "", widgetType: "", widgetModule: "", widget: null, widgetAttrs: {}, apply: function () {
}, validate: function () {
}, isRequired: function () {
}, equals: function (_d) {
}};
Spring.AbstractValidateAllDecoration = function () {
};
Spring.AbstractValidateAllDecoration.prototype = {event: "", elementId: "", apply: function () {
}, cleanup: function () {
}, handleEvent: function (_e) {
}, equals: function (_f) {
}};
Spring.AbstractCommandLinkDecoration = function () {
};
Spring.AbstractCommandLinkDecoration.prototype = {elementId: "", linkHtml: "", apply: function () {
}, submitFormFromLink: function (_10, _11, _12) {
}, equals: function (_13) {
}};
Spring.AbstractAjaxEventDecoration = function () {
};
Spring.AbstractAjaxEventDecoration.prototype = {event: "", elementId: "", sourceId: "", formId: "", popup: false, params: {}, apply: function () {
}, cleanup: function () {
}, submit: function (_14) {
}, equals: function (_15) {
}};
Spring.AbstractRemotingHandler = function () {
};
Spring.AbstractRemotingHandler.prototype = {submitForm: function (_16, _17, _18) {
}, getLinkedResource: function (_19, _1a, _1b) {
}, getResource: function (_1c, _1d, _1e) {
}, handleResponse: function () {
}, handleError: function () {
}};