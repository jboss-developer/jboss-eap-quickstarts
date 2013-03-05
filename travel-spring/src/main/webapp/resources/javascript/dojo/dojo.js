/*
 Copyright (c) 2004-2008, The Dojo Foundation All Rights Reserved.
 Available via Academic Free License >= 2.1 OR the modified BSD license.
 see: http://dojotoolkit.org/license for details
 */

/*
 This is a compiled version of Dojo, built for deployment and not for
 development. To get an editable version, please visit:

 http://dojotoolkit.org

 for documentation and information on getting the source.
 */

(function () {
    var _1 = null;
    if ((_1 || (typeof djConfig != "undefined" && djConfig.scopeMap)) && (typeof window != "undefined")) {
        var _2 = "", _3 = "", _4 = "", _5 = {}, _6 = {};
        _1 = _1 || djConfig.scopeMap;
        for (var i = 0; i < _1.length; i++) {
            var _8 = _1[i];
            _2 += "var " + _8[0] + " = {}; " + _8[1] + " = " + _8[0] + ";" + _8[1] + "._scopeName = '" + _8[1] + "';";
            _3 += (i == 0 ? "" : ",") + _8[0];
            _4 += (i == 0 ? "" : ",") + _8[1];
            _5[_8[0]] = _8[1];
            _6[_8[1]] = _8[0];
        }
        eval(_2 + "dojo._scopeArgs = [" + _4 + "];");
        dojo._scopePrefixArgs = _3;
        dojo._scopePrefix = "(function(" + _3 + "){";
        dojo._scopeSuffix = "})(" + _4 + ")";
        dojo._scopeMap = _5;
        dojo._scopeMapRev = _6;
    }
    (function () {
        if (!this["console"]) {
            this.console = {};
        }
        var cn = ["assert", "count", "debug", "dir", "dirxml", "error", "group", "groupEnd", "info", "profile", "profileEnd", "time", "timeEnd", "trace", "warn", "log"];
        var i = 0, tn;
        while ((tn = cn[i++])) {
            if (!console[tn]) {
                (function () {
                    var _c = tn + "";
                    console[_c] = ("log" in console) ? function () {
                        var a = Array.apply({}, arguments);
                        a.unshift(_c + ":");
                        console["log"](a.join(" "));
                    } : function () {
                    };
                })();
            }
        }
        if (typeof dojo == "undefined") {
            this.dojo = {_scopeName: "dojo", _scopePrefix: "", _scopePrefixArgs: "", _scopeSuffix: "", _scopeMap: {}, _scopeMapRev: {}};
        }
        var d = dojo;
        if (typeof dijit == "undefined") {
            this.dijit = {_scopeName: "dijit"};
        }
        if (typeof dojox == "undefined") {
            this.dojox = {_scopeName: "dojox"};
        }
        if (!d._scopeArgs) {
            d._scopeArgs = [dojo, dijit, dojox];
        }
        d.global = this;
        d.config = {isDebug: false, debugAtAllCosts: false};
        if (typeof djConfig != "undefined") {
            for (var _f in djConfig) {
                d.config[_f] = djConfig[_f];
            }
        }
        var _10 = ["Browser", "Rhino", "Spidermonkey", "Mobile"];
        var t;
        while ((t = _10.shift())) {
            d["is" + t] = false;
        }
        dojo.locale = d.config.locale;
        var rev = "$Rev: 21540 $".match(/\d+/);
        dojo.version = {major: 0, minor: 0, patch: 0, flag: "dev", revision: rev ? +rev[0] : 999999, toString: function () {
            with (d.version) {
                return major + "." + minor + "." + patch + flag + " (" + revision + ")";
            }
        }};
        if (typeof OpenAjax != "undefined") {
            OpenAjax.hub.registerLibrary(dojo._scopeName, "http://dojotoolkit.org", d.version.toString());
        }
        dojo._mixin = function (obj, _14) {
            var _15 = {};
            for (var x in _14) {
                if (_15[x] === undefined || _15[x] != _14[x]) {
                    obj[x] = _14[x];
                }
            }
            if (d["isIE"] && _14) {
                var p = _14.toString;
                if (typeof p == "function" && p != obj.toString && p != _15.toString && p != "\nfunction toString() {\n    [native code]\n}\n") {
                    obj.toString = _14.toString;
                }
            }
            return obj;
        };
        dojo.mixin = function (obj, _19) {
            for (var i = 1, l = arguments.length; i < l; i++) {
                d._mixin(obj, arguments[i]);
            }
            return obj;
        };
        dojo._getProp = function (_1c, _1d, _1e) {
            var obj = _1e || d.global;
            for (var i = 0, p; obj && (p = _1c[i]); i++) {
                if (i == 0 && this._scopeMap[p]) {
                    p = this._scopeMap[p];
                }
                obj = (p in obj ? obj[p] : (_1d ? obj[p] = {} : undefined));
            }
            return obj;
        };
        dojo.setObject = function (_22, _23, _24) {
            var _25 = _22.split("."), p = _25.pop(), obj = d._getProp(_25, true, _24);
            return obj && p ? (obj[p] = _23) : undefined;
        };
        dojo.getObject = function (_28, _29, _2a) {
            return d._getProp(_28.split("."), _29, _2a);
        };
        dojo.exists = function (_2b, obj) {
            return !!d.getObject(_2b, false, obj);
        };
        dojo["eval"] = function (_2d) {
            return d.global.eval ? d.global.eval(_2d) : eval(_2d);
        };
        d.deprecated = d.experimental = function () {
        };
    })();
    (function () {
        var d = dojo;
        d.mixin(d, {_loadedModules: {}, _inFlightCount: 0, _hasResource: {}, _modulePrefixes: {dojo: {name: "dojo", value: "."}, doh: {name: "doh", value: "../util/doh"}, tests: {name: "tests", value: "tests"}}, _moduleHasPrefix: function (_2f) {
            var mp = this._modulePrefixes;
            return !!(mp[_2f] && mp[_2f].value);
        }, _getModulePrefix: function (_31) {
            var mp = this._modulePrefixes;
            if (this._moduleHasPrefix(_31)) {
                return mp[_31].value;
            }
            return _31;
        }, _loadedUrls: [], _postLoad: false, _loaders: [], _unloaders: [], _loadNotifying: false});
        dojo._loadPath = function (_33, _34, cb) {
            var uri = ((_33.charAt(0) == "/" || _33.match(/^\w+:/)) ? "" : this.baseUrl) + _33;
            try {
                return !_34 ? this._loadUri(uri, cb) : this._loadUriAndCheck(uri, _34, cb);
            } catch (e) {
                console.error(e);
                return false;
            }
        };
        dojo._loadUri = function (uri, cb) {
            if (this._loadedUrls[uri]) {
                return true;
            }
            var _39 = this._getText(uri, true);
            if (!_39) {
                return false;
            }
            this._loadedUrls[uri] = true;
            this._loadedUrls.push(uri);
            if (cb) {
                _39 = "(" + _39 + ")";
            } else {
                _39 = this._scopePrefix + _39 + this._scopeSuffix;
            }
            if (d.isMoz) {
                _39 += "\r\n//@ sourceURL=" + uri;
            }
            var _3a = d["eval"](_39);
            if (cb) {
                cb(_3a);
            }
            return true;
        };
        dojo._loadUriAndCheck = function (uri, _3c, cb) {
            var ok = false;
            try {
                ok = this._loadUri(uri, cb);
            } catch (e) {
                console.error("failed loading " + uri + " with error: " + e);
            }
            return !!(ok && this._loadedModules[_3c]);
        };
        dojo.loaded = function () {
            this._loadNotifying = true;
            this._postLoad = true;
            var mll = d._loaders;
            this._loaders = [];
            for (var x = 0; x < mll.length; x++) {
                mll[x]();
            }
            this._loadNotifying = false;
            if (d._postLoad && d._inFlightCount == 0 && mll.length) {
                d._callLoaded();
            }
        };
        dojo.unloaded = function () {
            var mll = this._unloaders;
            while (mll.length) {
                (mll.pop())();
            }
        };
        d._onto = function (arr, obj, fn) {
            if (!fn) {
                arr.push(obj);
            } else {
                if (fn) {
                    var _45 = (typeof fn == "string") ? obj[fn] : fn;
                    arr.push(function () {
                        _45.call(obj);
                    });
                }
            }
        };
        dojo.addOnLoad = function (obj, _47) {
            d._onto(d._loaders, obj, _47);
            if (d._postLoad && d._inFlightCount == 0 && !d._loadNotifying) {
                d._callLoaded();
            }
        };
        var dca = d.config.addOnLoad;
        if (dca) {
            d.addOnLoad[(dca instanceof Array ? "apply" : "call")](d, dca);
        }
        dojo.addOnUnload = function (obj, _4a) {
            d._onto(d._unloaders, obj, _4a);
        };
        dojo._modulesLoaded = function () {
            if (d._postLoad) {
                return;
            }
            if (d._inFlightCount > 0) {
                console.warn("files still in flight!");
                return;
            }
            d._callLoaded();
        };
        dojo._callLoaded = function () {
            if (typeof setTimeout == "object" || (dojo.config.useXDomain && d.isOpera)) {
                if (dojo.isAIR) {
                    setTimeout(function () {
                        dojo.loaded();
                    }, 0);
                } else {
                    setTimeout(dojo._scopeName + ".loaded();", 0);
                }
            } else {
                d.loaded();
            }
        };
        dojo._getModuleSymbols = function (_4b) {
            var _4c = _4b.split(".");
            for (var i = _4c.length; i > 0; i--) {
                var _4e = _4c.slice(0, i).join(".");
                if ((i == 1) && !this._moduleHasPrefix(_4e)) {
                    _4c[0] = "../" + _4c[0];
                } else {
                    var _4f = this._getModulePrefix(_4e);
                    if (_4f != _4e) {
                        _4c.splice(0, i, _4f);
                        break;
                    }
                }
            }
            return _4c;
        };
        dojo._global_omit_module_check = false;
        dojo.loadInit = function (_50) {
            _50();
        };
        dojo._loadModule = dojo.require = function (_51, _52) {
            _52 = this._global_omit_module_check || _52;
            var _53 = this._loadedModules[_51];
            if (_53) {
                return _53;
            }
            var _54 = this._getModuleSymbols(_51).join("/") + ".js";
            var _55 = (!_52) ? _51 : null;
            var ok = this._loadPath(_54, _55);
            if (!ok && !_52) {
                throw new Error("Could not load '" + _51 + "'; last tried '" + _54 + "'");
            }
            if (!_52 && !this._isXDomain) {
                _53 = this._loadedModules[_51];
                if (!_53) {
                    throw new Error("symbol '" + _51 + "' is not defined after loading '" + _54 + "'");
                }
            }
            return _53;
        };
        dojo.provide = function (_57) {
            _57 = _57 + "";
            return (d._loadedModules[_57] = d.getObject(_57, true));
        };
        dojo.platformRequire = function (_58) {
            var _59 = _58.common || [];
            var _5a = _59.concat(_58[d._name] || _58["default"] || []);
            for (var x = 0; x < _5a.length; x++) {
                var _5c = _5a[x];
                if (_5c.constructor == Array) {
                    d._loadModule.apply(d, _5c);
                } else {
                    d._loadModule(_5c);
                }
            }
        };
        dojo.requireIf = function (_5d, _5e) {
            if (_5d === true) {
                var _5f = [];
                for (var i = 1; i < arguments.length; i++) {
                    _5f.push(arguments[i]);
                }
                d.require.apply(d, _5f);
            }
        };
        dojo.requireAfterIf = d.requireIf;
        dojo.registerModulePath = function (_61, _62) {
            d._modulePrefixes[_61] = {name: _61, value: _62};
        };
        dojo.requireLocalization = function (_63, _64, _65, _66) {
            d.require("dojo.i18n");
            d.i18n._requireLocalization.apply(d.hostenv, arguments);
        };
        var ore = new RegExp("^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\\?([^#]*))?(#(.*))?$");
        var ire = new RegExp("^((([^\\[:]+):)?([^@]+)@)?(\\[([^\\]]+)\\]|([^\\[:]*))(:([0-9]+))?$");
        dojo._Url = function () {
            var n = null;
            var _a = arguments;
            var uri = [_a[0]];
            for (var i = 1; i < _a.length; i++) {
                if (!_a[i]) {
                    continue;
                }
                var _6d = new d._Url(_a[i] + "");
                var _6e = new d._Url(uri[0] + "");
                if (_6d.path == "" && !_6d.scheme && !_6d.authority && !_6d.query) {
                    if (_6d.fragment != n) {
                        _6e.fragment = _6d.fragment;
                    }
                    _6d = _6e;
                } else {
                    if (!_6d.scheme) {
                        _6d.scheme = _6e.scheme;
                        if (!_6d.authority) {
                            _6d.authority = _6e.authority;
                            if (_6d.path.charAt(0) != "/") {
                                var _6f = _6e.path.substring(0, _6e.path.lastIndexOf("/") + 1) + _6d.path;
                                var _70 = _6f.split("/");
                                for (var j = 0; j < _70.length; j++) {
                                    if (_70[j] == ".") {
                                        if (j == _70.length - 1) {
                                            _70[j] = "";
                                        } else {
                                            _70.splice(j, 1);
                                            j--;
                                        }
                                    } else {
                                        if (j > 0 && !(j == 1 && _70[0] == "") && _70[j] == ".." && _70[j - 1] != "..") {
                                            if (j == (_70.length - 1)) {
                                                _70.splice(j, 1);
                                                _70[j - 1] = "";
                                            } else {
                                                _70.splice(j - 1, 2);
                                                j -= 2;
                                            }
                                        }
                                    }
                                }
                                _6d.path = _70.join("/");
                            }
                        }
                    }
                }
                uri = [];
                if (_6d.scheme) {
                    uri.push(_6d.scheme, ":");
                }
                if (_6d.authority) {
                    uri.push("//", _6d.authority);
                }
                uri.push(_6d.path);
                if (_6d.query) {
                    uri.push("?", _6d.query);
                }
                if (_6d.fragment) {
                    uri.push("#", _6d.fragment);
                }
            }
            this.uri = uri.join("");
            var r = this.uri.match(ore);
            this.scheme = r[2] || (r[1] ? "" : n);
            this.authority = r[4] || (r[3] ? "" : n);
            this.path = r[5];
            this.query = r[7] || (r[6] ? "" : n);
            this.fragment = r[9] || (r[8] ? "" : n);
            if (this.authority != n) {
                r = this.authority.match(ire);
                this.user = r[3] || n;
                this.password = r[4] || n;
                this.host = r[6] || r[7];
                this.port = r[9] || n;
            }
        };
        dojo._Url.prototype.toString = function () {
            return this.uri;
        };
        dojo.moduleUrl = function (_73, url) {
            var loc = d._getModuleSymbols(_73).join("/");
            if (!loc) {
                return null;
            }
            if (loc.lastIndexOf("/") != loc.length - 1) {
                loc += "/";
            }
            var _76 = loc.indexOf(":");
            if (loc.charAt(0) != "/" && (_76 == -1 || _76 > loc.indexOf("/"))) {
                loc = d.baseUrl + loc;
            }
            return new d._Url(loc, url);
        };
    })();
    if (typeof window != "undefined") {
        dojo.isBrowser = true;
        dojo._name = "browser";
        (function () {
            var d = dojo;
            if (document && document.getElementsByTagName) {
                var _78 = document.getElementsByTagName("script");
                var _79 = /dojo(\.xd)?\.js(\W|$)/i;
                for (var i = 0; i < _78.length; i++) {
                    var src = _78[i].getAttribute("src");
                    if (!src) {
                        continue;
                    }
                    var m = src.match(_79);
                    if (m) {
                        if (!d.config.baseUrl) {
                            d.config.baseUrl = src.substring(0, m.index);
                        }
                        var cfg = _78[i].getAttribute("djConfig");
                        if (cfg) {
                            var _7e = eval("({ " + cfg + " })");
                            for (var x in _7e) {
                                dojo.config[x] = _7e[x];
                            }
                        }
                        break;
                    }
                }
            }
            d.baseUrl = d.config.baseUrl;
            var n = navigator;
            var dua = n.userAgent;
            var dav = n.appVersion;
            var tv = parseFloat(dav);
            if (dua.indexOf("Opera") >= 0) {
                d.isOpera = tv;
            }
            var _84 = Math.max(dav.indexOf("WebKit"), dav.indexOf("Safari"), 0);
            if (_84) {
                d.isSafari = parseFloat(dav.split("Version/")[1]) || (parseFloat(dav.substr(_84 + 7)) > 419.3) ? 3 : 2;
            }
            if (dua.indexOf("AdobeAIR") >= 0) {
                d.isAIR = 1;
            }
            if (dav.indexOf("Konqueror") >= 0 || d.isSafari) {
                d.isKhtml = tv;
            }
            if (dua.indexOf("Gecko") >= 0 && !d.isKhtml) {
                d.isMozilla = d.isMoz = tv;
            }
            if (d.isMoz) {
                d.isFF = parseFloat(dua.split("Firefox/")[1]) || undefined;
            }
            if (document.all && !d.isOpera) {
                d.isIE = parseFloat(dav.split("MSIE ")[1]) || undefined;
            }
            if (dojo.isIE && window.location.protocol === "file:") {
                dojo.config.ieForceActiveXXhr = true;
            }
            var cm = document.compatMode;
            d.isQuirks = cm == "BackCompat" || cm == "QuirksMode" || d.isIE < 6;
            d.locale = dojo.config.locale || (d.isIE ? n.userLanguage : n.language).toLowerCase();
            d._XMLHTTP_PROGIDS = ["Msxml2.XMLHTTP", "Microsoft.XMLHTTP", "Msxml2.XMLHTTP.4.0"];
            d._xhrObj = function () {
                var _86 = null;
                var _87 = null;
                if (!dojo.isIE || !dojo.config.ieForceActiveXXhr) {
                    try {
                        _86 = new XMLHttpRequest();
                    } catch (e) {
                    }
                }
                if (!_86) {
                    for (var i = 0; i < 3; ++i) {
                        var _89 = d._XMLHTTP_PROGIDS[i];
                        try {
                            _86 = new ActiveXObject(_89);
                        } catch (e) {
                            _87 = e;
                        }
                        if (_86) {
                            d._XMLHTTP_PROGIDS = [_89];
                            break;
                        }
                    }
                }
                if (!_86) {
                    throw new Error("XMLHTTP not available: " + _87);
                }
                return _86;
            };
            d._isDocumentOk = function (_8a) {
                var _8b = _8a.status || 0;
                return (_8b >= 200 && _8b < 300) || _8b == 304 || _8b == 1223 || (!_8b && (location.protocol == "file:" || location.protocol == "chrome:"));
            };
            var _8c = window.location + "";
            var _8d = document.getElementsByTagName("base");
            var _8e = (_8d && _8d.length > 0);
            d._getText = function (uri, _90) {
                var _91 = this._xhrObj();
                if (!_8e && dojo._Url) {
                    uri = (new dojo._Url(_8c, uri)).toString();
                }
                if (d.config.cacheBust) {
                    uri += "";
                    uri += (uri.indexOf("?") == -1 ? "?" : "&") + String(d.config.cacheBust).replace(/\W+/g, "");
                }
                _91.open("GET", uri, false);
                try {
                    _91.send(null);
                    if (!d._isDocumentOk(_91)) {
                        var err = Error("Unable to load " + uri + " status:" + _91.status);
                        err.status = _91.status;
                        err.responseText = _91.responseText;
                        throw err;
                    }
                } catch (e) {
                    if (_90) {
                        return null;
                    }
                    throw e;
                }
                return _91.responseText;
            };
            d._windowUnloaders = [];
            d.windowUnloaded = function () {
                var mll = this._windowUnloaders;
                while (mll.length) {
                    (mll.pop())();
                }
            };
            d.addOnWindowUnload = function (obj, _95) {
                d._onto(d._windowUnloaders, obj, _95);
            };
        })();
        dojo._initFired = false;
        dojo._loadInit = function (e) {
            dojo._initFired = true;
            var _97 = (e && e.type) ? e.type.toLowerCase() : "load";
            if (arguments.callee.initialized || (_97 != "domcontentloaded" && _97 != "load")) {
                return;
            }
            arguments.callee.initialized = true;
            if ("_khtmlTimer" in dojo) {
                clearInterval(dojo._khtmlTimer);
                delete dojo._khtmlTimer;
            }
            if (dojo._inFlightCount == 0) {
                dojo._modulesLoaded();
            }
        };
        dojo._fakeLoadInit = function () {
            dojo._loadInit({type: "load"});
        };
        if (!dojo.config.afterOnLoad) {
            if (document.addEventListener) {
                if (dojo.isOpera || dojo.isFF >= 3 || (dojo.isMoz && dojo.config.enableMozDomContentLoaded === true)) {
                    document.addEventListener("DOMContentLoaded", dojo._loadInit, null);
                }
                window.addEventListener("load", dojo._loadInit, null);
            }
            if (dojo.isAIR) {
                window.addEventListener("load", dojo._loadInit, null);
            } else {
                if (/(WebKit|khtml)/i.test(navigator.userAgent)) {
                    dojo._khtmlTimer = setInterval(function () {
                        if (/loaded|complete/.test(document.readyState)) {
                            dojo._loadInit();
                        }
                    }, 10);
                }
            }
        }
        (function () {
            var _w = window;
            var _99 = function (_9a, fp) {
                var _9c = _w[_9a] || function () {
                };
                _w[_9a] = function () {
                    fp.apply(_w, arguments);
                    _9c.apply(_w, arguments);
                };
            };
            if (dojo.isIE) {
                if (!dojo.config.afterOnLoad) {
                    document.write("<scr" + "ipt defer src=\"//:\" " + "onreadystatechange=\"if(this.readyState=='complete'){" + dojo._scopeName + "._loadInit();}\">" + "</scr" + "ipt>");
                }
                try {
                    document.namespaces.add("v", "urn:schemas-microsoft-com:vml");
                    document.createStyleSheet().addRule("v\\:*", "behavior:url(#default#VML)");
                } catch (e) {
                }
            }
            _99("onbeforeunload", function () {
                dojo.unloaded();
            });
            _99("onunload", function () {
                dojo.windowUnloaded();
            });
        })();
    }
    (function () {
        var mp = dojo.config["modulePaths"];
        if (mp) {
            for (var _9e in mp) {
                dojo.registerModulePath(_9e, mp[_9e]);
            }
        }
    })();
    if (dojo.config.isDebug) {
        dojo.require("dojo._firebug.firebug");
    }
    if (dojo.config.debugAtAllCosts) {
        dojo.config.useXDomain = true;
        dojo.require("dojo._base._loader.loader_xd");
        dojo.require("dojo._base._loader.loader_debug");
    }
    if (!dojo._hasResource["dojo._base.lang"]) {
        dojo._hasResource["dojo._base.lang"] = true;
        dojo.provide("dojo._base.lang");
        dojo.isString = function (it) {
            return !!arguments.length && it != null && (typeof it == "string" || it instanceof String);
        };
        dojo.isArray = function (it) {
            return it && (it instanceof Array || typeof it == "array");
        };
        dojo.isFunction = (function () {
            var _a1 = function (it) {
                return it && (typeof it == "function" || it instanceof Function);
            };
            return dojo.isSafari ? function (it) {
                if (typeof it == "function" && it == "[object NodeList]") {
                    return false;
                }
                return _a1(it);
            } : _a1;
        })();
        dojo.isObject = function (it) {
            return it !== undefined && (it === null || typeof it == "object" || dojo.isArray(it) || dojo.isFunction(it));
        };
        dojo.isArrayLike = function (it) {
            var d = dojo;
            return it && it !== undefined && !d.isString(it) && !d.isFunction(it) && !(it.tagName && it.tagName.toLowerCase() == "form") && (d.isArray(it) || isFinite(it.length));
        };
        dojo.isAlien = function (it) {
            return it && !dojo.isFunction(it) && /\{\s*\[native code\]\s*\}/.test(String(it));
        };
        dojo.extend = function (_a8, _a9) {
            for (var i = 1, l = arguments.length; i < l; i++) {
                dojo._mixin(_a8.prototype, arguments[i]);
            }
            return _a8;
        };
        dojo._hitchArgs = function (_ac, _ad) {
            var pre = dojo._toArray(arguments, 2);
            var _af = dojo.isString(_ad);
            return function () {
                var _b0 = dojo._toArray(arguments);
                var f = _af ? (_ac || dojo.global)[_ad] : _ad;
                return f && f.apply(_ac || this, pre.concat(_b0));
            };
        };
        dojo.hitch = function (_b2, _b3) {
            if (arguments.length > 2) {
                return dojo._hitchArgs.apply(dojo, arguments);
            }
            if (!_b3) {
                _b3 = _b2;
                _b2 = null;
            }
            if (dojo.isString(_b3)) {
                _b2 = _b2 || dojo.global;
                if (!_b2[_b3]) {
                    throw (["dojo.hitch: scope[\"", _b3, "\"] is null (scope=\"", _b2, "\")"].join(""));
                }
                return function () {
                    return _b2[_b3].apply(_b2, arguments || []);
                };
            }
            return !_b2 ? _b3 : function () {
                return _b3.apply(_b2, arguments || []);
            };
        };
        dojo.delegate = dojo._delegate = (function () {
            function TMP() {
            };
            return function (obj, _b5) {
                TMP.prototype = obj;
                var tmp = new TMP();
                if (_b5) {
                    dojo._mixin(tmp, _b5);
                }
                return tmp;
            };
        })();
        (function () {
            var _b7 = function (obj, _b9, _ba) {
                return (_ba || []).concat(Array.prototype.slice.call(obj, _b9 || 0));
            };
            var _bb = function (obj, _bd, _be) {
                var arr = _be || [];
                for (var x = _bd || 0; x < obj.length; x++) {
                    arr.push(obj[x]);
                }
                return arr;
            };
            dojo._toArray = (!dojo.isIE) ? _b7 : function (obj) {
                return ((obj.item) ? _bb : _b7).apply(this, arguments);
            };
        })();
        dojo.partial = function (_c2) {
            var arr = [null];
            return dojo.hitch.apply(dojo, arr.concat(dojo._toArray(arguments)));
        };
        dojo.clone = function (o) {
            if (!o) {
                return o;
            }
            if (dojo.isArray(o)) {
                var r = [];
                for (var i = 0; i < o.length; ++i) {
                    r.push(dojo.clone(o[i]));
                }
                return r;
            }
            if (!dojo.isObject(o)) {
                return o;
            }
            if (o.nodeType && o.cloneNode) {
                return o.cloneNode(true);
            }
            if (o instanceof Date) {
                return new Date(o.getTime());
            }
            var r = new o.constructor();
            for (var i in o) {
                if (!(i in r) || r[i] != o[i]) {
                    r[i] = dojo.clone(o[i]);
                }
            }
            return r;
        };
        dojo.trim = function (str) {
            return str.replace(/^\s\s*/, "").replace(/\s\s*$/, "");
        };
    }
    if (!dojo._hasResource["dojo._base.declare"]) {
        dojo._hasResource["dojo._base.declare"] = true;
        dojo.provide("dojo._base.declare");
        dojo.declare = function (_c8, _c9, _ca) {
            var dd = arguments.callee, _cc;
            if (dojo.isArray(_c9)) {
                _cc = _c9;
                _c9 = _cc.shift();
            }
            if (_cc) {
                dojo.forEach(_cc, function (m) {
                    if (!m) {
                        throw (_c8 + ": mixin #" + i + " is null");
                    }
                    _c9 = dd._delegate(_c9, m);
                });
            }
            var _ce = dd._delegate(_c9);
            _ca = _ca || {};
            _ce.extend(_ca);
            dojo.extend(_ce, {declaredClass: _c8, _constructor: _ca.constructor});
            _ce.prototype.constructor = _ce;
            return dojo.setObject(_c8, _ce);
        };
        dojo.mixin(dojo.declare, {_delegate: function (_cf, _d0) {
            var bp = (_cf || 0).prototype, mp = (_d0 || 0).prototype, dd = dojo.declare;
            var _d4 = dd._makeCtor();
            dojo.mixin(_d4, {superclass: bp, mixin: mp, extend: dd._extend});
            if (_cf) {
                _d4.prototype = dojo._delegate(bp);
            }
            dojo.extend(_d4, dd._core, mp || 0, {_constructor: null, preamble: null});
            _d4.prototype.constructor = _d4;
            _d4.prototype.declaredClass = (bp || 0).declaredClass + "_" + (mp || 0).declaredClass;
            return _d4;
        }, _extend: function (_d5) {
            var i, fn;
            for (i in _d5) {
                if (dojo.isFunction(fn = _d5[i]) && !0[i]) {
                    fn.nom = i;
                    fn.ctor = this;
                }
            }
            dojo.extend(this, _d5);
        }, _makeCtor: function () {
            return function () {
                this._construct(arguments);
            };
        }, _core: {_construct: function (_d8) {
            var c = _d8.callee, s = c.superclass, ct = s && s.constructor, m = c.mixin, mct = m && m.constructor, a = _d8, ii, fn;
            if (a[0]) {
                if (((fn = a[0].preamble))) {
                    a = fn.apply(this, a) || a;
                }
            }
            if ((fn = c.prototype.preamble)) {
                a = fn.apply(this, a) || a;
            }
            if (ct && ct.apply) {
                ct.apply(this, a);
            }
            if (mct && mct.apply) {
                mct.apply(this, a);
            }
            if ((ii = c.prototype._constructor)) {
                ii.apply(this, _d8);
            }
            if (this.constructor.prototype == c.prototype && (ct = this.postscript)) {
                ct.apply(this, _d8);
            }
        }, _findMixin: function (_e1) {
            var c = this.constructor, p, m;
            while (c) {
                p = c.superclass;
                m = c.mixin;
                if (m == _e1 || (m instanceof _e1.constructor)) {
                    return p;
                }
                if (m && m._findMixin && (m = m._findMixin(_e1))) {
                    return m;
                }
                c = p && p.constructor;
            }
        }, _findMethod: function (_e5, _e6, _e7, has) {
            var p = _e7, c, m, f;
            do {
                c = p.constructor;
                m = c.mixin;
                if (m && (m = this._findMethod(_e5, _e6, m, has))) {
                    return m;
                }
                if ((f = p[_e5]) && (has == (f == _e6))) {
                    return p;
                }
                p = c.superclass;
            } while (p);
            return !has && (p = this._findMixin(_e7)) && this._findMethod(_e5, _e6, p, has);
        }, inherited: function (_ed, _ee, _ef) {
            var a = arguments;
            if (!dojo.isString(a[0])) {
                _ef = _ee;
                _ee = _ed;
                _ed = _ee.callee.nom;
            }
            a = _ef || _ee;
            var c = _ee.callee, p = this.constructor.prototype, fn, mp;
            if (this[_ed] != c || p[_ed] == c) {
                mp = (c.ctor || 0).superclass || this._findMethod(_ed, c, p, true);
                if (!mp) {
                    throw (this.declaredClass + ": inherited method \"" + _ed + "\" mismatch");
                }
                p = this._findMethod(_ed, c, mp, false);
            }
            fn = p && p[_ed];
            if (!fn) {
                throw (mp.declaredClass + ": inherited method \"" + _ed + "\" not found");
            }
            return fn.apply(this, a);
        }}});
    }
    if (!dojo._hasResource["dojo._base.connect"]) {
        dojo._hasResource["dojo._base.connect"] = true;
        dojo.provide("dojo._base.connect");
        dojo._listener = {getDispatcher: function () {
            return function () {
                var ap = Array.prototype, c = arguments.callee, ls = c._listeners, t = c.target;
                var r = t && t.apply(this, arguments);
                var lls;
                lls = [].concat(ls);
                for (var i in lls) {
                    if (!(i in ap)) {
                        lls[i].apply(this, arguments);
                    }
                }
                return r;
            };
        }, add: function (_fc, _fd, _fe) {
            _fc = _fc || dojo.global;
            var f = _fc[_fd];
            if (!f || !f._listeners) {
                var d = dojo._listener.getDispatcher();
                d.target = f;
                d._listeners = [];
                f = _fc[_fd] = d;
            }
            return f._listeners.push(_fe);
        }, remove: function (_101, _102, _103) {
            var f = (_101 || dojo.global)[_102];
            if (f && f._listeners && _103--) {
                delete f._listeners[_103];
            }
        }};
        dojo.connect = function (obj, _106, _107, _108, _109) {
            var a = arguments, args = [], i = 0;
            args.push(dojo.isString(a[0]) ? null : a[i++], a[i++]);
            var a1 = a[i + 1];
            args.push(dojo.isString(a1) || dojo.isFunction(a1) ? a[i++] : null, a[i++]);
            for (var l = a.length; i < l; i++) {
                args.push(a[i]);
            }
            return dojo._connect.apply(this, args);
        };
        dojo._connect = function (obj, _10f, _110, _111) {
            var l = dojo._listener, h = l.add(obj, _10f, dojo.hitch(_110, _111));
            return [obj, _10f, h, l];
        };
        dojo.disconnect = function (_114) {
            if (_114 && _114[0] !== undefined) {
                dojo._disconnect.apply(this, _114);
                delete _114[0];
            }
        };
        dojo._disconnect = function (obj, _116, _117, _118) {
            _118.remove(obj, _116, _117);
        };
        dojo._topics = {};
        dojo.subscribe = function (_119, _11a, _11b) {
            return [_119, dojo._listener.add(dojo._topics, _119, dojo.hitch(_11a, _11b))];
        };
        dojo.unsubscribe = function (_11c) {
            if (_11c) {
                dojo._listener.remove(dojo._topics, _11c[0], _11c[1]);
            }
        };
        dojo.publish = function (_11d, args) {
            var f = dojo._topics[_11d];
            if (f) {
                f.apply(this, args || []);
            }
        };
        dojo.connectPublisher = function (_120, obj, _122) {
            var pf = function () {
                dojo.publish(_120, arguments);
            };
            return (_122) ? dojo.connect(obj, _122, pf) : dojo.connect(obj, pf);
        };
    }
    if (!dojo._hasResource["dojo._base.Deferred"]) {
        dojo._hasResource["dojo._base.Deferred"] = true;
        dojo.provide("dojo._base.Deferred");
        dojo.Deferred = function (_124) {
            this.chain = [];
            this.id = this._nextId();
            this.fired = -1;
            this.paused = 0;
            this.results = [null, null];
            this.canceller = _124;
            this.silentlyCancelled = false;
        };
        dojo.extend(dojo.Deferred, {_nextId: (function () {
            var n = 1;
            return function () {
                return n++;
            };
        })(), cancel: function () {
            var err;
            if (this.fired == -1) {
                if (this.canceller) {
                    err = this.canceller(this);
                } else {
                    this.silentlyCancelled = true;
                }
                if (this.fired == -1) {
                    if (!(err instanceof Error)) {
                        var res = err;
                        err = new Error("Deferred Cancelled");
                        err.dojoType = "cancel";
                        err.cancelResult = res;
                    }
                    this.errback(err);
                }
            } else {
                if ((this.fired == 0) && (this.results[0] instanceof dojo.Deferred)) {
                    this.results[0].cancel();
                }
            }
        }, _resback: function (res) {
            this.fired = ((res instanceof Error) ? 1 : 0);
            this.results[this.fired] = res;
            this._fire();
        }, _check: function () {
            if (this.fired != -1) {
                if (!this.silentlyCancelled) {
                    throw new Error("already called!");
                }
                this.silentlyCancelled = false;
                return;
            }
        }, callback: function (res) {
            this._check();
            this._resback(res);
        }, errback: function (res) {
            this._check();
            if (!(res instanceof Error)) {
                res = new Error(res);
            }
            this._resback(res);
        }, addBoth: function (cb, cbfn) {
            var _12d = dojo.hitch.apply(dojo, arguments);
            return this.addCallbacks(_12d, _12d);
        }, addCallback: function (cb, cbfn) {
            return this.addCallbacks(dojo.hitch.apply(dojo, arguments));
        }, addErrback: function (cb, cbfn) {
            return this.addCallbacks(null, dojo.hitch.apply(dojo, arguments));
        }, addCallbacks: function (cb, eb) {
            this.chain.push([cb, eb]);
            if (this.fired >= 0) {
                this._fire();
            }
            return this;
        }, _fire: function () {
            var _134 = this.chain;
            var _135 = this.fired;
            var res = this.results[_135];
            var self = this;
            var cb = null;
            while ((_134.length > 0) && (this.paused == 0)) {
                var f = _134.shift()[_135];
                if (!f) {
                    continue;
                }
                var func = function () {
                    var ret = f(res);
                    if (typeof ret != "undefined") {
                        res = ret;
                    }
                    _135 = ((res instanceof Error) ? 1 : 0);
                    if (res instanceof dojo.Deferred) {
                        cb = function (res) {
                            self._resback(res);
                            self.paused--;
                            if ((self.paused == 0) && (self.fired >= 0)) {
                                self._fire();
                            }
                        };
                        this.paused++;
                    }
                };
                if (dojo.config.isDebug) {
                    func.call(this);
                } else {
                    try {
                        func.call(this);
                    } catch (err) {
                        _135 = 1;
                        res = err;
                    }
                }
            }
            this.fired = _135;
            this.results[_135] = res;
            if ((cb) && (this.paused)) {
                res.addBoth(cb);
            }
        }});
    }
    if (!dojo._hasResource["dojo._base.json"]) {
        dojo._hasResource["dojo._base.json"] = true;
        dojo.provide("dojo._base.json");
        dojo.fromJson = function (json) {
            return eval("(" + json + ")");
        };
        dojo._escapeString = function (str) {
            return ("\"" + str.replace(/(["\\])/g, "\\$1") + "\"").replace(/[\f]/g, "\\f").replace(/[\b]/g, "\\b").replace(/[\n]/g, "\\n").replace(/[\t]/g, "\\t").replace(/[\r]/g, "\\r");
        };
        dojo.toJsonIndentStr = "\t";
        dojo.toJson = function (it, _140, _141) {
            if (it === undefined) {
                return "undefined";
            }
            var _142 = typeof it;
            if (_142 == "number" || _142 == "boolean") {
                return it + "";
            }
            if (it === null) {
                return "null";
            }
            if (dojo.isString(it)) {
                return dojo._escapeString(it);
            }
            var _143 = arguments.callee;
            var _144;
            _141 = _141 || "";
            var _145 = _140 ? _141 + dojo.toJsonIndentStr : "";
            var tf = it.__json__ || it.json;
            if (dojo.isFunction(tf)) {
                _144 = tf.call(it);
                if (it !== _144) {
                    return _143(_144, _140, _145);
                }
            }
            if (it.nodeType && it.cloneNode) {
                throw new Error("Can't serialize DOM nodes");
            }
            var sep = _140 ? " " : "";
            var _148 = _140 ? "\n" : "";
            if (dojo.isArray(it)) {
                var res = dojo.map(it, function (obj) {
                    var val = _143(obj, _140, _145);
                    if (typeof val != "string") {
                        val = "undefined";
                    }
                    return _148 + _145 + val;
                });
                return "[" + res.join("," + sep) + _148 + _141 + "]";
            }
            if (_142 == "function") {
                return null;
            }
            var _14c = [], key;
            for (key in it) {
                var _14e, val;
                if (typeof key == "number") {
                    _14e = "\"" + key + "\"";
                } else {
                    if (typeof key == "string") {
                        _14e = dojo._escapeString(key);
                    } else {
                        continue;
                    }
                }
                val = _143(it[key], _140, _145);
                if (typeof val != "string") {
                    continue;
                }
                _14c.push(_148 + _145 + _14e + ":" + sep + val);
            }
            return "{" + _14c.join("," + sep) + _148 + _141 + "}";
        };
    }
    if (!dojo._hasResource["dojo._base.array"]) {
        dojo._hasResource["dojo._base.array"] = true;
        dojo.provide("dojo._base.array");
        (function () {
            var _150 = function (arr, obj, cb) {
                return [dojo.isString(arr) ? arr.split("") : arr, obj || dojo.global, dojo.isString(cb) ? new Function("item", "index", "array", cb) : cb];
            };
            dojo.mixin(dojo, {indexOf: function (_154, _155, _156, _157) {
                var step = 1, end = _154.length || 0, i = 0;
                if (_157) {
                    i = end - 1;
                    step = end = -1;
                }
                if (_156 != undefined) {
                    i = _156;
                }
                if ((_157 && i > end) || i < end) {
                    for (; i != end; i += step) {
                        if (_154[i] == _155) {
                            return i;
                        }
                    }
                }
                return -1;
            }, lastIndexOf: function (_15a, _15b, _15c) {
                return dojo.indexOf(_15a, _15b, _15c, true);
            }, forEach: function (arr, _15e, _15f) {
                if (!arr || !arr.length) {
                    return;
                }
                var _p = _150(arr, _15f, _15e);
                arr = _p[0];
                for (var i = 0, l = arr.length; i < l; ++i) {
                    _p[2].call(_p[1], arr[i], i, arr);
                }
            }, _everyOrSome: function (_163, arr, _165, _166) {
                var _p = _150(arr, _166, _165);
                arr = _p[0];
                for (var i = 0, l = arr.length; i < l; ++i) {
                    var _16a = !!_p[2].call(_p[1], arr[i], i, arr);
                    if (_163 ^ _16a) {
                        return _16a;
                    }
                }
                return _163;
            }, every: function (arr, _16c, _16d) {
                return this._everyOrSome(true, arr, _16c, _16d);
            }, some: function (arr, _16f, _170) {
                return this._everyOrSome(false, arr, _16f, _170);
            }, map: function (arr, _172, _173) {
                var _p = _150(arr, _173, _172);
                arr = _p[0];
                var _175 = (arguments[3] ? (new arguments[3]()) : []);
                for (var i = 0, l = arr.length; i < l; ++i) {
                    _175.push(_p[2].call(_p[1], arr[i], i, arr));
                }
                return _175;
            }, filter: function (arr, _179, _17a) {
                var _p = _150(arr, _17a, _179);
                arr = _p[0];
                var _17c = [];
                for (var i = 0, l = arr.length; i < l; ++i) {
                    if (_p[2].call(_p[1], arr[i], i, arr)) {
                        _17c.push(arr[i]);
                    }
                }
                return _17c;
            }});
        })();
    }
    if (!dojo._hasResource["dojo._base.Color"]) {
        dojo._hasResource["dojo._base.Color"] = true;
        dojo.provide("dojo._base.Color");
        dojo.Color = function (_17f) {
            if (_17f) {
                this.setColor(_17f);
            }
        };
        dojo.Color.named = {black: [0, 0, 0], silver: [192, 192, 192], gray: [128, 128, 128], white: [255, 255, 255], maroon: [128, 0, 0], red: [255, 0, 0], purple: [128, 0, 128], fuchsia: [255, 0, 255], green: [0, 128, 0], lime: [0, 255, 0], olive: [128, 128, 0], yellow: [255, 255, 0], navy: [0, 0, 128], blue: [0, 0, 255], teal: [0, 128, 128], aqua: [0, 255, 255]};
        dojo.extend(dojo.Color, {r: 255, g: 255, b: 255, a: 1, _set: function (r, g, b, a) {
            var t = this;
            t.r = r;
            t.g = g;
            t.b = b;
            t.a = a;
        }, setColor: function (_185) {
            var d = dojo;
            if (d.isString(_185)) {
                d.colorFromString(_185, this);
            } else {
                if (d.isArray(_185)) {
                    d.colorFromArray(_185, this);
                } else {
                    this._set(_185.r, _185.g, _185.b, _185.a);
                    if (!(_185 instanceof d.Color)) {
                        this.sanitize();
                    }
                }
            }
            return this;
        }, sanitize: function () {
            return this;
        }, toRgb: function () {
            var t = this;
            return [t.r, t.g, t.b];
        }, toRgba: function () {
            var t = this;
            return [t.r, t.g, t.b, t.a];
        }, toHex: function () {
            var arr = dojo.map(["r", "g", "b"], function (x) {
                var s = this[x].toString(16);
                return s.length < 2 ? "0" + s : s;
            }, this);
            return "#" + arr.join("");
        }, toCss: function (_18c) {
            var t = this, rgb = t.r + ", " + t.g + ", " + t.b;
            return (_18c ? "rgba(" + rgb + ", " + t.a : "rgb(" + rgb) + ")";
        }, toString: function () {
            return this.toCss(true);
        }});
        dojo.blendColors = function (_18f, end, _191, obj) {
            var d = dojo, t = obj || new dojo.Color();
            d.forEach(["r", "g", "b", "a"], function (x) {
                t[x] = _18f[x] + (end[x] - _18f[x]) * _191;
                if (x != "a") {
                    t[x] = Math.round(t[x]);
                }
            });
            return t.sanitize();
        };
        dojo.colorFromRgb = function (_196, obj) {
            var m = _196.toLowerCase().match(/^rgba?\(([\s\.,0-9]+)\)/);
            return m && dojo.colorFromArray(m[1].split(/\s*,\s*/), obj);
        };
        dojo.colorFromHex = function (_199, obj) {
            var d = dojo, t = obj || new d.Color(), bits = (_199.length == 4) ? 4 : 8, mask = (1 << bits) - 1;
            _199 = Number("0x" + _199.substr(1));
            if (isNaN(_199)) {
                return null;
            }
            d.forEach(["b", "g", "r"], function (x) {
                var c = _199 & mask;
                _199 >>= bits;
                t[x] = bits == 4 ? 17 * c : c;
            });
            t.a = 1;
            return t;
        };
        dojo.colorFromArray = function (a, obj) {
            var t = obj || new dojo.Color();
            t._set(Number(a[0]), Number(a[1]), Number(a[2]), Number(a[3]));
            if (isNaN(t.a)) {
                t.a = 1;
            }
            return t.sanitize();
        };
        dojo.colorFromString = function (str, obj) {
            var a = dojo.Color.named[str];
            return a && dojo.colorFromArray(a, obj) || dojo.colorFromRgb(str, obj) || dojo.colorFromHex(str, obj);
        };
    }
    if (!dojo._hasResource["dojo._base"]) {
        dojo._hasResource["dojo._base"] = true;
        dojo.provide("dojo._base");
    }
    if (!dojo._hasResource["dojo._base.window"]) {
        dojo._hasResource["dojo._base.window"] = true;
        dojo.provide("dojo._base.window");
        dojo.doc = window["document"] || null;
        dojo.body = function () {
            return dojo.doc.body || dojo.doc.getElementsByTagName("body")[0];
        };
        dojo.setContext = function (_1a7, _1a8) {
            dojo.global = _1a7;
            dojo.doc = _1a8;
        };
        dojo._fireCallback = function (_1a9, _1aa, _1ab) {
            if (_1aa && dojo.isString(_1a9)) {
                _1a9 = _1aa[_1a9];
            }
            return _1a9.apply(_1aa, _1ab || []);
        };
        dojo.withGlobal = function (_1ac, _1ad, _1ae, _1af) {
            var rval;
            var _1b1 = dojo.global;
            var _1b2 = dojo.doc;
            try {
                dojo.setContext(_1ac, _1ac.document);
                rval = dojo._fireCallback(_1ad, _1ae, _1af);
            } finally {
                dojo.setContext(_1b1, _1b2);
            }
            return rval;
        };
        dojo.withDoc = function (_1b3, _1b4, _1b5, _1b6) {
            var rval;
            var _1b8 = dojo.doc;
            try {
                dojo.doc = _1b3;
                rval = dojo._fireCallback(_1b4, _1b5, _1b6);
            } finally {
                dojo.doc = _1b8;
            }
            return rval;
        };
    }
    if (!dojo._hasResource["dojo._base.event"]) {
        dojo._hasResource["dojo._base.event"] = true;
        dojo.provide("dojo._base.event");
        (function () {
            var del = (dojo._event_listener = {add: function (node, name, fp) {
                if (!node) {
                    return;
                }
                name = del._normalizeEventName(name);
                fp = del._fixCallback(name, fp);
                var _1bd = name;
                if (!dojo.isIE && (name == "mouseenter" || name == "mouseleave")) {
                    var ofp = fp;
                    name = (name == "mouseenter") ? "mouseover" : "mouseout";
                    fp = function (e) {
                        try {
                            e.relatedTarget.tagName;
                        } catch (e2) {
                            return;
                        }
                        if (!dojo.isDescendant(e.relatedTarget, node)) {
                            return ofp.call(this, e);
                        }
                    };
                }
                node.addEventListener(name, fp, false);
                return fp;
            }, remove: function (node, _1c1, _1c2) {
                if (node) {
                    _1c1 = del._normalizeEventName(_1c1);
                    if (!dojo.isIE && (_1c1 == "mouseenter" || _1c1 == "mouseleave")) {
                        _1c1 = (_1c1 == "mouseenter") ? "mouseover" : "mouseout";
                    }
                    node.removeEventListener(_1c1, _1c2, false);
                }
            }, _normalizeEventName: function (name) {
                return name.slice(0, 2) == "on" ? name.slice(2) : name;
            }, _fixCallback: function (name, fp) {
                return name != "keypress" ? fp : function (e) {
                    return fp.call(this, del._fixEvent(e, this));
                };
            }, _fixEvent: function (evt, _1c8) {
                switch (evt.type) {
                    case "keypress":
                        del._setKeyChar(evt);
                        break;
                }
                return evt;
            }, _setKeyChar: function (evt) {
                evt.keyChar = evt.charCode ? String.fromCharCode(evt.charCode) : "";
                evt.charOrCode = evt.keyChar || evt.keyCode;
            }, _punctMap: {106: 42, 111: 47, 186: 59, 187: 43, 188: 44, 189: 45, 190: 46, 191: 47, 192: 96, 219: 91, 220: 92, 221: 93, 222: 39}});
            dojo.fixEvent = function (evt, _1cb) {
                return del._fixEvent(evt, _1cb);
            };
            dojo.stopEvent = function (evt) {
                evt.preventDefault();
                evt.stopPropagation();
            };
            var _1cd = dojo._listener;
            dojo._connect = function (obj, _1cf, _1d0, _1d1, _1d2) {
                var _1d3 = obj && (obj.nodeType || obj.attachEvent || obj.addEventListener);
                var lid = !_1d3 ? 0 : (!_1d2 ? 1 : 2), l = [dojo._listener, del, _1cd][lid];
                var h = l.add(obj, _1cf, dojo.hitch(_1d0, _1d1));
                return [obj, _1cf, h, lid];
            };
            dojo._disconnect = function (obj, _1d8, _1d9, _1da) {
                ([dojo._listener, del, _1cd][_1da]).remove(obj, _1d8, _1d9);
            };
            dojo.keys = {BACKSPACE: 8, TAB: 9, CLEAR: 12, ENTER: 13, SHIFT: 16, CTRL: 17, ALT: 18, PAUSE: 19, CAPS_LOCK: 20, ESCAPE: 27, SPACE: 32, PAGE_UP: 33, PAGE_DOWN: 34, END: 35, HOME: 36, LEFT_ARROW: 37, UP_ARROW: 38, RIGHT_ARROW: 39, DOWN_ARROW: 40, INSERT: 45, DELETE: 46, HELP: 47, LEFT_WINDOW: 91, RIGHT_WINDOW: 92, SELECT: 93, NUMPAD_0: 96, NUMPAD_1: 97, NUMPAD_2: 98, NUMPAD_3: 99, NUMPAD_4: 100, NUMPAD_5: 101, NUMPAD_6: 102, NUMPAD_7: 103, NUMPAD_8: 104, NUMPAD_9: 105, NUMPAD_MULTIPLY: 106, NUMPAD_PLUS: 107, NUMPAD_ENTER: 108, NUMPAD_MINUS: 109, NUMPAD_PERIOD: 110, NUMPAD_DIVIDE: 111, F1: 112, F2: 113, F3: 114, F4: 115, F5: 116, F6: 117, F7: 118, F8: 119, F9: 120, F10: 121, F11: 122, F12: 123, F13: 124, F14: 125, F15: 126, NUM_LOCK: 144, SCROLL_LOCK: 145};
            if (dojo.isIE) {
                var _1db = function (e, code) {
                    try {
                        return (e.keyCode = code);
                    } catch (e) {
                        return 0;
                    }
                };
                var iel = dojo._listener;
                var _1df = dojo._ieListenersName = "_" + dojo._scopeName + "_listeners";
                if (!dojo.config._allow_leaks) {
                    _1cd = iel = dojo._ie_listener = {handlers: [], add: function (_1e0, _1e1, _1e2) {
                        _1e0 = _1e0 || dojo.global;
                        var f = _1e0[_1e1];
                        if (!f || !f[_1df]) {
                            var d = dojo._getIeDispatcher();
                            d.target = f && (ieh.push(f) - 1);
                            d[_1df] = [];
                            f = _1e0[_1e1] = d;
                        }
                        return f[_1df].push(ieh.push(_1e2) - 1);
                    }, remove: function (_1e6, _1e7, _1e8) {
                        var f = (_1e6 || dojo.global)[_1e7], l = f && f[_1df];
                        if (f && l && _1e8--) {
                            delete ieh[l[_1e8]];
                            delete l[_1e8];
                        }
                    }};
                    var ieh = iel.handlers;
                }
                dojo.mixin(del, {add: function (node, _1ec, fp) {
                    if (!node) {
                        return;
                    }
                    _1ec = del._normalizeEventName(_1ec);
                    if (_1ec == "onkeypress") {
                        var kd = node.onkeydown;
                        if (!kd || !kd[_1df] || !kd._stealthKeydownHandle) {
                            var h = del.add(node, "onkeydown", del._stealthKeyDown);
                            kd = node.onkeydown;
                            kd._stealthKeydownHandle = h;
                            kd._stealthKeydownRefs = 1;
                        } else {
                            kd._stealthKeydownRefs++;
                        }
                    }
                    return iel.add(node, _1ec, del._fixCallback(fp));
                }, remove: function (node, _1f1, _1f2) {
                    _1f1 = del._normalizeEventName(_1f1);
                    iel.remove(node, _1f1, _1f2);
                    if (_1f1 == "onkeypress") {
                        var kd = node.onkeydown;
                        if (--kd._stealthKeydownRefs <= 0) {
                            iel.remove(node, "onkeydown", kd._stealthKeydownHandle);
                            delete kd._stealthKeydownHandle;
                        }
                    }
                }, _normalizeEventName: function (_1f4) {
                    return _1f4.slice(0, 2) != "on" ? "on" + _1f4 : _1f4;
                }, _nop: function () {
                }, _fixEvent: function (evt, _1f6) {
                    if (!evt) {
                        var w = _1f6 && (_1f6.ownerDocument || _1f6.document || _1f6).parentWindow || window;
                        evt = w.event;
                    }
                    if (!evt) {
                        return (evt);
                    }
                    evt.target = evt.srcElement;
                    evt.currentTarget = (_1f6 || evt.srcElement);
                    evt.layerX = evt.offsetX;
                    evt.layerY = evt.offsetY;
                    var se = evt.srcElement, doc = (se && se.ownerDocument) || document;
                    var _1fa = ((dojo.isIE < 6) || (doc["compatMode"] == "BackCompat")) ? doc.body : doc.documentElement;
                    var _1fb = dojo._getIeDocumentElementOffset();
                    evt.pageX = evt.clientX + dojo._fixIeBiDiScrollLeft(_1fa.scrollLeft || 0) - _1fb.x;
                    evt.pageY = evt.clientY + (_1fa.scrollTop || 0) - _1fb.y;
                    if (evt.type == "mouseover") {
                        evt.relatedTarget = evt.fromElement;
                    }
                    if (evt.type == "mouseout") {
                        evt.relatedTarget = evt.toElement;
                    }
                    evt.stopPropagation = del._stopPropagation;
                    evt.preventDefault = del._preventDefault;
                    return del._fixKeys(evt);
                }, _fixKeys: function (evt) {
                    switch (evt.type) {
                        case "keypress":
                            var c = ("charCode" in evt ? evt.charCode : evt.keyCode);
                            if (c == 10) {
                                c = 0;
                                evt.keyCode = 13;
                            } else {
                                if (c == 13 || c == 27) {
                                    c = 0;
                                } else {
                                    if (c == 3) {
                                        c = 99;
                                    }
                                }
                            }
                            evt.charCode = c;
                            del._setKeyChar(evt);
                            break;
                    }
                    return evt;
                }, _stealthKeyDown: function (evt) {
                    var kp = evt.currentTarget.onkeypress;
                    if (!kp || !kp[_1df]) {
                        return;
                    }
                    var k = evt.keyCode;
                    var _201 = (k != 13) && (k != 32) && (k != 27) && (k < 48 || k > 90) && (k < 96 || k > 111) && (k < 186 || k > 192) && (k < 219 || k > 222);
                    if (_201 || evt.ctrlKey) {
                        var c = _201 ? 0 : k;
                        if (evt.ctrlKey) {
                            if (k == 3 || k == 13) {
                                return;
                            } else {
                                if (c > 95 && c < 106) {
                                    c -= 48;
                                } else {
                                    if ((!evt.shiftKey) && (c >= 65 && c <= 90)) {
                                        c += 32;
                                    } else {
                                        c = del._punctMap[c] || c;
                                    }
                                }
                            }
                        }
                        var faux = del._synthesizeEvent(evt, {type: "keypress", faux: true, charCode: c});
                        kp.call(evt.currentTarget, faux);
                        evt.cancelBubble = faux.cancelBubble;
                        evt.returnValue = faux.returnValue;
                        _1db(evt, faux.keyCode);
                    }
                }, _stopPropagation: function () {
                    this.cancelBubble = true;
                }, _preventDefault: function () {
                    this.bubbledKeyCode = this.keyCode;
                    if (this.ctrlKey) {
                        _1db(this, 0);
                    }
                    this.returnValue = false;
                }});
                dojo.stopEvent = function (evt) {
                    evt = evt || window.event;
                    del._stopPropagation.call(evt);
                    del._preventDefault.call(evt);
                };
            }
            del._synthesizeEvent = function (evt, _206) {
                var faux = dojo.mixin({}, evt, _206);
                del._setKeyChar(faux);
                faux.preventDefault = function () {
                    evt.preventDefault();
                };
                faux.stopPropagation = function () {
                    evt.stopPropagation();
                };
                return faux;
            };
            if (dojo.isOpera) {
                dojo.mixin(del, {_fixEvent: function (evt, _209) {
                    switch (evt.type) {
                        case "keypress":
                            var c = evt.which;
                            if (c == 3) {
                                c = 99;
                            }
                            c = ((c < 41) && (!evt.shiftKey) ? 0 : c);
                            if ((evt.ctrlKey) && (!evt.shiftKey) && (c >= 65) && (c <= 90)) {
                                c += 32;
                            }
                            return del._synthesizeEvent(evt, {charCode: c});
                    }
                    return evt;
                }});
            }
            if (dojo.isSafari) {
                del._add = del.add;
                del._remove = del.remove;
                dojo.mixin(del, {add: function (node, _20c, fp) {
                    if (!node) {
                        return;
                    }
                    var _20e = del._add(node, _20c, fp);
                    if (del._normalizeEventName(_20c) == "keypress") {
                        _20e._stealthKeyDownHandle = del._add(node, "keydown", function (evt) {
                            var k = evt.keyCode;
                            var _211 = (k != 13) && (k != 32) && (k != 27) && (k < 48 || k > 90) && (k < 96 || k > 111) && (k < 186 || k > 192) && (k < 219 || k > 222);
                            if (_211 || evt.ctrlKey) {
                                var c = _211 ? 0 : k;
                                if (evt.ctrlKey) {
                                    if (k == 3 || k == 13) {
                                        return;
                                    } else {
                                        if (c > 95 && c < 106) {
                                            c -= 48;
                                        } else {
                                            if ((!evt.shiftKey) && (c >= 65 && c <= 90)) {
                                                c += 32;
                                            } else {
                                                c = del._punctMap[c] || c;
                                            }
                                        }
                                    }
                                }
                                var faux = del._synthesizeEvent(evt, {type: "keypress", faux: true, charCode: c});
                                fp.call(evt.currentTarget, faux);
                            }
                        });
                    }
                    return _20e;
                }, remove: function (node, _215, _216) {
                    if (node) {
                        if (_216._stealthKeyDownHandle) {
                            del._remove(node, "keydown", _216._stealthKeyDownHandle);
                        }
                        del._remove(node, _215, _216);
                    }
                }, _fixEvent: function (evt, _218) {
                    switch (evt.type) {
                        case "keypress":
                            if (evt.faux) {
                                return evt;
                            }
                            var c = evt.charCode;
                            c = c >= 32 ? c : 0;
                            return del._synthesizeEvent(evt, {charCode: c, faux: true});
                    }
                    return evt;
                }});
            }
        })();
        if (dojo.isIE) {
            dojo._ieDispatcher = function (args, _21b) {
                var ap = Array.prototype, h = dojo._ie_listener.handlers, c = args.callee, ls = c[dojo._ieListenersName], t = h[c.target];
                var r = t && t.apply(_21b, args);
                var lls = [].concat(ls);
                for (var i in lls) {
                    if (!(i in ap)) {
                        h[lls[i]].apply(_21b, args);
                    }
                }
                return r;
            };
            dojo._getIeDispatcher = function () {
                return new Function(dojo._scopeName + "._ieDispatcher(arguments, this)");
            };
            dojo._event_listener._fixCallback = function (fp) {
                var f = dojo._event_listener._fixEvent;
                return function (e) {
                    return fp.call(this, f(e, this));
                };
            };
        }
    }
    if (!dojo._hasResource["dojo._base.html"]) {
        dojo._hasResource["dojo._base.html"] = true;
        dojo.provide("dojo._base.html");
        try {
            document.execCommand("BackgroundImageCache", false, true);
        } catch (e) {
        }
        if (dojo.isIE || dojo.isOpera) {
            dojo.byId = function (id, doc) {
                if (dojo.isString(id)) {
                    var _d = doc || dojo.doc;
                    var te = _d.getElementById(id);
                    if (te && te.attributes.id.value == id) {
                        return te;
                    } else {
                        var eles = _d.all[id];
                        if (!eles || !eles.length) {
                            return eles;
                        }
                        var i = 0;
                        while ((te = eles[i++])) {
                            if (te.attributes.id.value == id) {
                                return te;
                            }
                        }
                    }
                } else {
                    return id;
                }
            };
        } else {
            dojo.byId = function (id, doc) {
                return dojo.isString(id) ? (doc || dojo.doc).getElementById(id) : id;
            };
        }
        (function () {
            var d = dojo;
            var _230 = null;
            dojo.addOnWindowUnload(function () {
                _230 = null;
            });
            dojo._destroyElement = function (node) {
                node = d.byId(node);
                try {
                    if (!_230 || _230.ownerDocument != node.ownerDocument) {
                        _230 = node.ownerDocument.createElement("div");
                    }
                    _230.appendChild(node.parentNode ? node.parentNode.removeChild(node) : node);
                    _230.innerHTML = "";
                } catch (e) {
                }
            };
            dojo.isDescendant = function (node, _233) {
                try {
                    node = d.byId(node);
                    _233 = d.byId(_233);
                    while (node) {
                        if (node === _233) {
                            return true;
                        }
                        node = node.parentNode;
                    }
                } catch (e) {
                }
                return false;
            };
            dojo.setSelectable = function (node, _235) {
                node = d.byId(node);
                if (d.isMozilla) {
                    node.style.MozUserSelect = _235 ? "" : "none";
                } else {
                    if (d.isKhtml) {
                        node.style.KhtmlUserSelect = _235 ? "auto" : "none";
                    } else {
                        if (d.isIE) {
                            var v = (node.unselectable = _235 ? "" : "on");
                            d.query("*", node).forEach("item.unselectable = '" + v + "'");
                        }
                    }
                }
            };
            var _237 = function (node, ref) {
                ref.parentNode.insertBefore(node, ref);
                return true;
            };
            var _23a = function (node, ref) {
                var pn = ref.parentNode;
                if (ref == pn.lastChild) {
                    pn.appendChild(node);
                } else {
                    return _237(node, ref.nextSibling);
                }
                return true;
            };
            dojo.place = function (node, _23f, _240) {
                if (!node || !_23f) {
                    return false;
                }
                node = d.byId(node);
                _23f = d.byId(_23f);
                if (typeof _240 == "number") {
                    var cn = _23f.childNodes;
                    if (!cn.length || cn.length <= _240) {
                        _23f.appendChild(node);
                        return true;
                    }
                    return _237(node, _240 <= 0 ? _23f.firstChild : cn[_240]);
                }
                switch (_240) {
                    case "before":
                        return _237(node, _23f);
                    case "after":
                        return _23a(node, _23f);
                    case "first":
                        if (_23f.firstChild) {
                            return _237(node, _23f.firstChild);
                        }
                    default:
                        _23f.appendChild(node);
                        return true;
                }
            };
            dojo.boxModel = "content-box";
            if (d.isIE) {
                var _dcm = document.compatMode;
                d.boxModel = _dcm == "BackCompat" || _dcm == "QuirksMode" || d.isIE < 6 ? "border-box" : "content-box";
            }
            var gcs;
            if (d.isSafari) {
                gcs = function (node) {
                    var s;
                    if (node instanceof HTMLElement) {
                        var dv = node.ownerDocument.defaultView;
                        s = dv.getComputedStyle(node, null);
                        if (!s && node.style) {
                            node.style.display = "";
                            s = dv.getComputedStyle(node, null);
                        }
                    }
                    return s || {};
                };
            } else {
                if (d.isIE) {
                    gcs = function (node) {
                        return node.nodeType == 1 ? node.currentStyle : {};
                    };
                } else {
                    gcs = function (node) {
                        return node instanceof HTMLElement ? node.ownerDocument.defaultView.getComputedStyle(node, null) : {};
                    };
                }
            }
            dojo.getComputedStyle = gcs;
            if (!d.isIE) {
                dojo._toPixelValue = function (_249, _24a) {
                    return parseFloat(_24a) || 0;
                };
            } else {
                dojo._toPixelValue = function (_24b, _24c) {
                    if (!_24c) {
                        return 0;
                    }
                    if (_24c == "medium") {
                        return 4;
                    }
                    if (_24c.slice && (_24c.slice(-2) == "px")) {
                        return parseFloat(_24c);
                    }
                    with (_24b) {
                        var _24d = style.left;
                        var _24e = runtimeStyle.left;
                        runtimeStyle.left = currentStyle.left;
                        try {
                            style.left = _24c;
                            _24c = style.pixelLeft;
                        } catch (e) {
                            _24c = 0;
                        }
                        style.left = _24d;
                        runtimeStyle.left = _24e;
                    }
                    return _24c;
                };
            }
            var px = d._toPixelValue;
            var astr = "DXImageTransform.Microsoft.Alpha";
            var af = function (n, f) {
                try {
                    return n.filters.item(astr);
                } catch (e) {
                    return f ? {} : null;
                }
            };
            dojo._getOpacity = d.isIE ? function (node) {
                try {
                    return af(node).Opacity / 100;
                } catch (e) {
                    return 1;
                }
            } : function (node) {
                return gcs(node).opacity;
            };
            dojo._setOpacity = d.isIE ? function (node, _257) {
                var ov = _257 * 100;
                node.style.zoom = 1;
                af(node, 1).Enabled = (_257 == 1 ? false : true);
                if (!af(node)) {
                    node.style.filter += " progid:" + astr + "(Opacity=" + ov + ")";
                } else {
                    af(node, 1).Opacity = ov;
                }
                if (node.nodeName.toLowerCase() == "tr") {
                    d.query("> td", node).forEach(function (i) {
                        d._setOpacity(i, _257);
                    });
                }
                return _257;
            } : function (node, _25b) {
                return node.style.opacity = _25b;
            };
            var _25c = {left: true, top: true};
            var _25d = /margin|padding|width|height|max|min|offset/;
            var _25e = function (node, type, _261) {
                type = type.toLowerCase();
                if (d.isIE) {
                    if (_261 == "auto") {
                        if (type == "height") {
                            return node.offsetHeight;
                        }
                        if (type == "width") {
                            return node.offsetWidth;
                        }
                    }
                    if (type == "fontweight") {
                        switch (_261) {
                            case 700:
                                return "bold";
                            case 400:
                            default:
                                return "normal";
                        }
                    }
                }
                if (!(type in _25c)) {
                    _25c[type] = _25d.test(type);
                }
                return _25c[type] ? px(node, _261) : _261;
            };
            var _262 = d.isIE ? "styleFloat" : "cssFloat";
            var _263 = {"cssFloat": _262, "styleFloat": _262, "float": _262};
            dojo.style = function (node, _265, _266) {
                var n = d.byId(node), args = arguments.length, op = (_265 == "opacity");
                _265 = _263[_265] || _265;
                if (args == 3) {
                    return op ? d._setOpacity(n, _266) : n.style[_265] = _266;
                }
                if (args == 2 && op) {
                    return d._getOpacity(n);
                }
                var s = gcs(n);
                if (args == 2 && !d.isString(_265)) {
                    for (var x in _265) {
                        d.style(node, x, _265[x]);
                    }
                    return s;
                }
                return (args == 1) ? s : _25e(n, _265, s[_265] || n.style[_265]);
            };
            dojo._getPadExtents = function (n, _26d) {
                var s = _26d || gcs(n), l = px(n, s.paddingLeft), t = px(n, s.paddingTop);
                return {l: l, t: t, w: l + px(n, s.paddingRight), h: t + px(n, s.paddingBottom)};
            };
            dojo._getBorderExtents = function (n, _272) {
                var ne = "none", s = _272 || gcs(n), bl = (s.borderLeftStyle != ne ? px(n, s.borderLeftWidth) : 0), bt = (s.borderTopStyle != ne ? px(n, s.borderTopWidth) : 0);
                return {l: bl, t: bt, w: bl + (s.borderRightStyle != ne ? px(n, s.borderRightWidth) : 0), h: bt + (s.borderBottomStyle != ne ? px(n, s.borderBottomWidth) : 0)};
            };
            dojo._getPadBorderExtents = function (n, _278) {
                var s = _278 || gcs(n), p = d._getPadExtents(n, s), b = d._getBorderExtents(n, s);
                return {l: p.l + b.l, t: p.t + b.t, w: p.w + b.w, h: p.h + b.h};
            };
            dojo._getMarginExtents = function (n, _27d) {
                var s = _27d || gcs(n), l = px(n, s.marginLeft), t = px(n, s.marginTop), r = px(n, s.marginRight), b = px(n, s.marginBottom);
                if (d.isSafari && (s.position != "absolute")) {
                    r = l;
                }
                return {l: l, t: t, w: l + r, h: t + b};
            };
            dojo._getMarginBox = function (node, _284) {
                var s = _284 || gcs(node), me = d._getMarginExtents(node, s);
                var l = node.offsetLeft - me.l, t = node.offsetTop - me.t, p = node.parentNode;
                if (d.isMoz) {
                    var sl = parseFloat(s.left), st = parseFloat(s.top);
                    if (!isNaN(sl) && !isNaN(st)) {
                        l = sl, t = st;
                    } else {
                        if (p && p.style) {
                            var pcs = gcs(p);
                            if (pcs.overflow != "visible") {
                                var be = d._getBorderExtents(p, pcs);
                                l += be.l, t += be.t;
                            }
                        }
                    }
                } else {
                    if (d.isOpera) {
                        if (p) {
                            var be = d._getBorderExtents(p);
                            l -= be.l;
                            t -= be.t;
                        }
                    }
                }
                return {l: l, t: t, w: node.offsetWidth + me.w, h: node.offsetHeight + me.h};
            };
            dojo._getContentBox = function (node, _28f) {
                var s = _28f || gcs(node), pe = d._getPadExtents(node, s), be = d._getBorderExtents(node, s), w = node.clientWidth, h;
                if (!w) {
                    w = node.offsetWidth, h = node.offsetHeight;
                } else {
                    h = node.clientHeight, be.w = be.h = 0;
                }
                if (d.isOpera) {
                    pe.l += be.l;
                    pe.t += be.t;
                }
                return {l: pe.l, t: pe.t, w: w - pe.w - be.w, h: h - pe.h - be.h};
            };
            dojo._getBorderBox = function (node, _296) {
                var s = _296 || gcs(node), pe = d._getPadExtents(node, s), cb = d._getContentBox(node, s);
                return {l: cb.l - pe.l, t: cb.t - pe.t, w: cb.w + pe.w, h: cb.h + pe.h};
            };
            dojo._setBox = function (node, l, t, w, h, u) {
                u = u || "px";
                var s = node.style;
                if (!isNaN(l)) {
                    s.left = l + u;
                }
                if (!isNaN(t)) {
                    s.top = t + u;
                }
                if (w >= 0) {
                    s.width = w + u;
                }
                if (h >= 0) {
                    s.height = h + u;
                }
            };
            dojo._isButtonTag = function (node) {
                return node.tagName == "BUTTON" || node.tagName == "INPUT" && node.getAttribute("type").toUpperCase() == "BUTTON";
            };
            dojo._usesBorderBox = function (node) {
                var n = node.tagName;
                return d.boxModel == "border-box" || n == "TABLE" || dojo._isButtonTag(node);
            };
            dojo._setContentSize = function (node, _2a5, _2a6, _2a7) {
                if (d._usesBorderBox(node)) {
                    var pb = d._getPadBorderExtents(node, _2a7);
                    if (_2a5 >= 0) {
                        _2a5 += pb.w;
                    }
                    if (_2a6 >= 0) {
                        _2a6 += pb.h;
                    }
                }
                d._setBox(node, NaN, NaN, _2a5, _2a6);
            };
            dojo._setMarginBox = function (node, _2aa, _2ab, _2ac, _2ad, _2ae) {
                var s = _2ae || gcs(node);
                var bb = d._usesBorderBox(node), pb = bb ? _2b2 : d._getPadBorderExtents(node, s);
                if (dojo.isSafari) {
                    if (dojo._isButtonTag(node)) {
                        var ns = node.style;
                        if (_2ac >= 0 && !ns.width) {
                            ns.width = "4px";
                        }
                        if (_2ad >= 0 && !ns.height) {
                            ns.height = "4px";
                        }
                    }
                }
                var mb = d._getMarginExtents(node, s);
                if (_2ac >= 0) {
                    _2ac = Math.max(_2ac - pb.w - mb.w, 0);
                }
                if (_2ad >= 0) {
                    _2ad = Math.max(_2ad - pb.h - mb.h, 0);
                }
                d._setBox(node, _2aa, _2ab, _2ac, _2ad);
            };
            var _2b2 = {l: 0, t: 0, w: 0, h: 0};
            dojo.marginBox = function (node, box) {
                var n = d.byId(node), s = gcs(n), b = box;
                return !b ? d._getMarginBox(n, s) : d._setMarginBox(n, b.l, b.t, b.w, b.h, s);
            };
            dojo.contentBox = function (node, box) {
                var n = d.byId(node), s = gcs(n), b = box;
                return !b ? d._getContentBox(n, s) : d._setContentSize(n, b.w, b.h, s);
            };
            var _2bf = function (node, prop) {
                if (!(node = (node || 0).parentNode)) {
                    return 0;
                }
                var val, _2c3 = 0, _b = d.body();
                while (node && node.style) {
                    if (gcs(node).position == "fixed") {
                        return 0;
                    }
                    val = node[prop];
                    if (val) {
                        _2c3 += val - 0;
                        if (node == _b) {
                            break;
                        }
                    }
                    node = node.parentNode;
                }
                return _2c3;
            };
            dojo._docScroll = function () {
                var _b = d.body(), _w = d.global, de = d.doc.documentElement;
                return {y: (_w.pageYOffset || de.scrollTop || _b.scrollTop || 0), x: (_w.pageXOffset || d._fixIeBiDiScrollLeft(de.scrollLeft) || _b.scrollLeft || 0)};
            };
            dojo._isBodyLtr = function () {
                return !("_bodyLtr" in d) ? d._bodyLtr = gcs(d.body()).direction == "ltr" : d._bodyLtr;
            };
            dojo._getIeDocumentElementOffset = function () {
                var de = d.doc.documentElement;
                return (d.isIE >= 7) ? {x: de.getBoundingClientRect().left, y: de.getBoundingClientRect().top} : {x: d._isBodyLtr() || window.parent == window ? de.clientLeft : de.offsetWidth - de.clientWidth - de.clientLeft, y: de.clientTop};
            };
            dojo._fixIeBiDiScrollLeft = function (_2c9) {
                var dd = d.doc;
                if (d.isIE && !dojo._isBodyLtr()) {
                    var de = dd.compatMode == "BackCompat" ? dd.body : dd.documentElement;
                    return _2c9 + de.clientWidth - de.scrollWidth;
                }
                return _2c9;
            };
            dojo._abs = function (node, _2cd) {
                var _2ce = node.ownerDocument;
                var ret = {x: 0, y: 0};
                var db = d.body();
                if (d.isIE || (d.isFF >= 3)) {
                    var _2d1 = node.getBoundingClientRect();
                    var cs;
                    if (d.isFF) {
                        var dv = node.ownerDocument.defaultView;
                        cs = dv.getComputedStyle(db.parentNode, null);
                    }
                    var _2d4 = (d.isIE) ? d._getIeDocumentElementOffset() : {x: px(db.parentNode, cs.marginLeft), y: px(db.parentNode, cs.marginTop)};
                    ret.x = _2d1.left - _2d4.x;
                    ret.y = _2d1.top - _2d4.y;
                } else {
                    if (node["offsetParent"]) {
                        var _2d5;
                        if (d.isSafari && (gcs(node).position == "absolute") && (node.parentNode == db)) {
                            _2d5 = db;
                        } else {
                            _2d5 = db.parentNode;
                        }
                        var cs = gcs(node);
                        var n = node;
                        if (d.isOpera && cs.position != "absolute") {
                            n = n.offsetParent;
                        }
                        ret.x -= _2bf(n, "scrollLeft");
                        ret.y -= _2bf(n, "scrollTop");
                        var _2d7 = node;
                        do {
                            var n = _2d7.offsetLeft;
                            if (!d.isOpera || n > 0) {
                                ret.x += isNaN(n) ? 0 : n;
                            }
                            var t = _2d7.offsetTop;
                            ret.y += isNaN(t) ? 0 : t;
                            var cs = gcs(_2d7);
                            if (_2d7 != node) {
                                if (d.isSafari) {
                                    ret.x += px(_2d7, cs.borderLeftWidth);
                                    ret.y += px(_2d7, cs.borderTopWidth);
                                } else {
                                    if (d.isFF) {
                                        ret.x += 2 * px(_2d7, cs.borderLeftWidth);
                                        ret.y += 2 * px(_2d7, cs.borderTopWidth);
                                    }
                                }
                            }
                            if (d.isFF && cs.position == "static") {
                                var _2d9 = _2d7.parentNode;
                                while (_2d9 != _2d7.offsetParent) {
                                    var pcs = gcs(_2d9);
                                    if (pcs.position == "static") {
                                        ret.x += px(_2d7, pcs.borderLeftWidth);
                                        ret.y += px(_2d7, pcs.borderTopWidth);
                                    }
                                    _2d9 = _2d9.parentNode;
                                }
                            }
                            _2d7 = _2d7.offsetParent;
                        } while ((_2d7 != _2d5) && _2d7);
                    } else {
                        if (node.x && node.y) {
                            ret.x += isNaN(node.x) ? 0 : node.x;
                            ret.y += isNaN(node.y) ? 0 : node.y;
                        }
                    }
                }
                if (_2cd) {
                    var _2db = d._docScroll();
                    ret.y += _2db.y;
                    ret.x += _2db.x;
                }
                return ret;
            };
            dojo.coords = function (node, _2dd) {
                var n = d.byId(node), s = gcs(n), mb = d._getMarginBox(n, s);
                var abs = d._abs(n, _2dd);
                mb.x = abs.x;
                mb.y = abs.y;
                return mb;
            };
            var _2e2 = d.isIE < 8;
            var _2e3 = function (name) {
                switch (name.toLowerCase()) {
                    case "tabindex":
                        return _2e2 ? "tabIndex" : "tabindex";
                    case "for":
                    case "htmlfor":
                        return _2e2 ? "htmlFor" : "for";
                    case "class":
                        return d.isIE ? "className" : "class";
                    default:
                        return name;
                }
            };
            var _2e5 = {colspan: "colSpan", enctype: "enctype", frameborder: "frameborder", method: "method", rowspan: "rowSpan", scrolling: "scrolling", shape: "shape", span: "span", type: "type", valuetype: "valueType"};
            dojo.hasAttr = function (node, name) {
                node = d.byId(node);
                var _2e8 = _2e3(name);
                _2e8 = _2e8 == "htmlFor" ? "for" : _2e8;
                var attr = node.getAttributeNode && node.getAttributeNode(_2e8);
                return attr ? attr.specified : false;
            };
            var _2ea = {};
            var _ctr = 0;
            var _2ec = dojo._scopeName + "attrid";
            dojo.attr = function (node, name, _2ef) {
                var args = arguments.length;
                if (args == 2 && !d.isString(name)) {
                    for (var x in name) {
                        d.attr(node, x, name[x]);
                    }
                    return;
                }
                node = d.byId(node);
                name = _2e3(name);
                if (args == 3) {
                    if (d.isFunction(_2ef)) {
                        var _2f2 = d.attr(node, _2ec);
                        if (!_2f2) {
                            _2f2 = _ctr++;
                            d.attr(node, _2ec, _2f2);
                        }
                        if (!_2ea[_2f2]) {
                            _2ea[_2f2] = {};
                        }
                        var h = _2ea[_2f2][name];
                        if (h) {
                            d.disconnect(h);
                        } else {
                            try {
                                delete node[name];
                            } catch (e) {
                            }
                        }
                        _2ea[_2f2][name] = d.connect(node, name, _2ef);
                    } else {
                        if ((typeof _2ef == "boolean") || (name == "innerHTML")) {
                            node[name] = _2ef;
                        } else {
                            if ((name == "style") && (!d.isString(_2ef))) {
                                d.style(node, _2ef);
                            } else {
                                node.setAttribute(name, _2ef);
                            }
                        }
                    }
                    return;
                } else {
                    var prop = _2e5[name.toLowerCase()];
                    if (prop) {
                        return node[prop];
                    } else {
                        var _2f5 = node[name];
                        return (typeof _2f5 == "boolean" || typeof _2f5 == "function") ? _2f5 : (d.hasAttr(node, name) ? node.getAttribute(name) : null);
                    }
                }
            };
            dojo.removeAttr = function (node, name) {
                d.byId(node).removeAttribute(_2e3(name));
            };
            var _2f8 = "className";
            dojo.hasClass = function (node, _2fa) {
                return ((" " + d.byId(node)[_2f8] + " ").indexOf(" " + _2fa + " ") >= 0);
            };
            dojo.addClass = function (node, _2fc) {
                node = d.byId(node);
                var cls = node[_2f8];
                if ((" " + cls + " ").indexOf(" " + _2fc + " ") < 0) {
                    node[_2f8] = cls + (cls ? " " : "") + _2fc;
                }
            };
            dojo.removeClass = function (node, _2ff) {
                node = d.byId(node);
                var t = d.trim((" " + node[_2f8] + " ").replace(" " + _2ff + " ", " "));
                if (node[_2f8] != t) {
                    node[_2f8] = t;
                }
            };
            dojo.toggleClass = function (node, _302, _303) {
                if (_303 === undefined) {
                    _303 = !d.hasClass(node, _302);
                }
                d[_303 ? "addClass" : "removeClass"](node, _302);
            };
        })();
    }
    if (!dojo._hasResource["dojo._base.NodeList"]) {
        dojo._hasResource["dojo._base.NodeList"] = true;
        dojo.provide("dojo._base.NodeList");
        (function () {
            var d = dojo;
            var tnl = function (arr) {
                arr.constructor = dojo.NodeList;
                dojo._mixin(arr, dojo.NodeList.prototype);
                return arr;
            };
            var _307 = function (func, _309) {
                return function () {
                    var _a = arguments;
                    var aa = d._toArray(_a, 0, [null]);
                    var s = this.map(function (i) {
                        aa[0] = i;
                        return d[func].apply(d, aa);
                    });
                    return (_309 || ((_a.length > 1) || !d.isString(_a[0]))) ? this : s;
                };
            };
            dojo.NodeList = function () {
                return tnl(Array.apply(null, arguments));
            };
            dojo.NodeList._wrap = tnl;
            dojo.extend(dojo.NodeList, {slice: function () {
                var a = d._toArray(arguments);
                return tnl(a.slice.apply(this, a));
            }, splice: function () {
                var a = d._toArray(arguments);
                return tnl(a.splice.apply(this, a));
            }, concat: function () {
                var a = d._toArray(arguments, 0, [this]);
                return tnl(a.concat.apply([], a));
            }, indexOf: function (_311, _312) {
                return d.indexOf(this, _311, _312);
            }, lastIndexOf: function () {
                return d.lastIndexOf.apply(d, d._toArray(arguments, 0, [this]));
            }, every: function (_313, _314) {
                return d.every(this, _313, _314);
            }, some: function (_315, _316) {
                return d.some(this, _315, _316);
            }, map: function (func, obj) {
                return d.map(this, func, obj, d.NodeList);
            }, forEach: function (_319, _31a) {
                d.forEach(this, _319, _31a);
                return this;
            }, coords: function () {
                return d.map(this, d.coords);
            }, attr: _307("attr"), style: _307("style"), addClass: _307("addClass", true), removeClass: _307("removeClass", true), toggleClass: _307("toggleClass", true), connect: _307("connect", true), place: function (_31b, _31c) {
                var item = d.query(_31b)[0];
                return this.forEach(function (i) {
                    d.place(i, item, _31c);
                });
            }, orphan: function (_31f) {
                return (_31f ? d._filterQueryResult(this, _31f) : this).forEach("if(item.parentNode){ item.parentNode.removeChild(item); }");
            }, adopt: function (_320, _321) {
                var item = this[0];
                return d.query(_320).forEach(function (ai) {
                    d.place(ai, item, _321 || "last");
                });
            }, query: function (_324) {
                if (!_324) {
                    return this;
                }
                var ret = d.NodeList();
                this.forEach(function (item) {
                    ret = ret.concat(d.query(_324, item).filter(function (_327) {
                        return (_327 !== undefined);
                    }));
                });
                return ret;
            }, filter: function (_328) {
                var _329 = this;
                var _a = arguments;
                var r = d.NodeList();
                var rp = function (t) {
                    if (t !== undefined) {
                        r.push(t);
                    }
                };
                if (d.isString(_328)) {
                    _329 = d._filterQueryResult(this, _a[0]);
                    if (_a.length == 1) {
                        return _329;
                    }
                    _a.shift();
                }
                d.forEach(d.filter(_329, _a[0], _a[1]), rp);
                return r;
            }, addContent: function (_32e, _32f) {
                var ta = d.doc.createElement("span");
                if (d.isString(_32e)) {
                    ta.innerHTML = _32e;
                } else {
                    ta.appendChild(_32e);
                }
                if (_32f === undefined) {
                    _32f = "last";
                }
                var ct = (_32f == "first" || _32f == "after") ? "lastChild" : "firstChild";
                this.forEach(function (item) {
                    var tn = ta.cloneNode(true);
                    while (tn[ct]) {
                        d.place(tn[ct], item, _32f);
                    }
                });
                return this;
            }, empty: function () {
                return this.forEach("item.innerHTML='';");
            }, instantiate: function (_334, _335) {
                var c = d.isFunction(_334) ? _334 : d.getObject(_334);
                return this.forEach(function (i) {
                    new c(_335 || {}, i);
                });
            }, at: function () {
                var nl = new dojo.NodeList();
                dojo.forEach(arguments, function (i) {
                    if (this[i]) {
                        nl.push(this[i]);
                    }
                }, this);
                return nl;
            }});
            d.forEach(["blur", "focus", "click", "keydown", "keypress", "keyup", "mousedown", "mouseenter", "mouseleave", "mousemove", "mouseout", "mouseover", "mouseup", "submit", "load", "error"], function (evt) {
                var _oe = "on" + evt;
                d.NodeList.prototype[_oe] = function (a, b) {
                    return this.connect(_oe, a, b);
                };
            });
        })();
    }
    if (!dojo._hasResource["dojo._base.query"]) {
        dojo._hasResource["dojo._base.query"] = true;
        dojo.provide("dojo._base.query");
        (function () {
            var d = dojo;
            var _33f = dojo.isIE ? "children" : "childNodes";
            var _340 = false;
            var _341 = function (_342) {
                if (">~+".indexOf(_342.charAt(_342.length - 1)) >= 0) {
                    _342 += " *";
                }
                _342 += " ";
                var ts = function (s, e) {
                    return d.trim(_342.slice(s, e));
                };
                var _346 = [];
                var _347 = -1;
                var _348 = -1;
                var _349 = -1;
                var _34a = -1;
                var _34b = -1;
                var inId = -1;
                var _34d = -1;
                var lc = "";
                var cc = "";
                var _350;
                var x = 0;
                var ql = _342.length;
                var _353 = null;
                var _cp = null;
                var _355 = function () {
                    if (_34d >= 0) {
                        var tv = (_34d == x) ? null : ts(_34d, x);
                        _353[(">~+".indexOf(tv) < 0) ? "tag" : "oper"] = tv;
                        _34d = -1;
                    }
                };
                var _357 = function () {
                    if (inId >= 0) {
                        _353.id = ts(inId, x).replace(/\\/g, "");
                        inId = -1;
                    }
                };
                var _358 = function () {
                    if (_34b >= 0) {
                        _353.classes.push(ts(_34b + 1, x).replace(/\\/g, ""));
                        _34b = -1;
                    }
                };
                var _359 = function () {
                    _357();
                    _355();
                    _358();
                };
                for (; lc = cc, cc = _342.charAt(x), x < ql; x++) {
                    if (lc == "\\") {
                        continue;
                    }
                    if (!_353) {
                        _350 = x;
                        _353 = {query: null, pseudos: [], attrs: [], classes: [], tag: null, oper: null, id: null};
                        _34d = x;
                    }
                    if (_347 >= 0) {
                        if (cc == "]") {
                            if (!_cp.attr) {
                                _cp.attr = ts(_347 + 1, x);
                            } else {
                                _cp.matchFor = ts((_349 || _347 + 1), x);
                            }
                            var cmf = _cp.matchFor;
                            if (cmf) {
                                if ((cmf.charAt(0) == "\"") || (cmf.charAt(0) == "'")) {
                                    _cp.matchFor = cmf.substring(1, cmf.length - 1);
                                }
                            }
                            _353.attrs.push(_cp);
                            _cp = null;
                            _347 = _349 = -1;
                        } else {
                            if (cc == "=") {
                                var _35b = ("|~^$*".indexOf(lc) >= 0) ? lc : "";
                                _cp.type = _35b + cc;
                                _cp.attr = ts(_347 + 1, x - _35b.length);
                                _349 = x + 1;
                            }
                        }
                    } else {
                        if (_348 >= 0) {
                            if (cc == ")") {
                                if (_34a >= 0) {
                                    _cp.value = ts(_348 + 1, x);
                                }
                                _34a = _348 = -1;
                            }
                        } else {
                            if (cc == "#") {
                                _359();
                                inId = x + 1;
                            } else {
                                if (cc == ".") {
                                    _359();
                                    _34b = x;
                                } else {
                                    if (cc == ":") {
                                        _359();
                                        _34a = x;
                                    } else {
                                        if (cc == "[") {
                                            _359();
                                            _347 = x;
                                            _cp = {};
                                        } else {
                                            if (cc == "(") {
                                                if (_34a >= 0) {
                                                    _cp = {name: ts(_34a + 1, x), value: null};
                                                    _353.pseudos.push(_cp);
                                                }
                                                _348 = x;
                                            } else {
                                                if (cc == " " && lc != cc) {
                                                    _359();
                                                    if (_34a >= 0) {
                                                        _353.pseudos.push({name: ts(_34a + 1, x)});
                                                    }
                                                    _353.hasLoops = (_353.pseudos.length || _353.attrs.length || _353.classes.length);
                                                    _353.query = ts(_350, x);
                                                    _353.otag = _353.tag = (_353["oper"]) ? null : (_353.tag || "*");
                                                    if (_353.tag) {
                                                        _353.tag = _353.tag.toUpperCase();
                                                    }
                                                    _346.push(_353);
                                                    _353 = null;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return _346;
            };
            var _35c = {"*=": function (attr, _35e) {
                return "[contains(@" + attr + ", '" + _35e + "')]";
            }, "^=": function (attr, _360) {
                return "[starts-with(@" + attr + ", '" + _360 + "')]";
            }, "$=": function (attr, _362) {
                return "[substring(@" + attr + ", string-length(@" + attr + ")-" + (_362.length - 1) + ")='" + _362 + "']";
            }, "~=": function (attr, _364) {
                return "[contains(concat(' ',@" + attr + ",' '), ' " + _364 + " ')]";
            }, "|=": function (attr, _366) {
                return "[contains(concat(' ',@" + attr + ",' '), ' " + _366 + "-')]";
            }, "=": function (attr, _368) {
                return "[@" + attr + "='" + _368 + "']";
            }};
            var _369 = function (_36a, _36b, _36c, _36d) {
                d.forEach(_36b.attrs, function (attr) {
                    var _36f;
                    if (attr.type && _36a[attr.type]) {
                        _36f = _36a[attr.type](attr.attr, attr.matchFor);
                    } else {
                        if (attr.attr.length) {
                            _36f = _36c(attr.attr);
                        }
                    }
                    if (_36f) {
                        _36d(_36f);
                    }
                });
            };
            var _370 = function (_371) {
                var _372 = ".";
                var _373 = _341(d.trim(_371));
                while (_373.length) {
                    var tqp = _373.shift();
                    var _375;
                    var _376 = "";
                    if (tqp.oper == ">") {
                        _375 = "/";
                        tqp = _373.shift();
                    } else {
                        if (tqp.oper == "~") {
                            _375 = "/following-sibling::";
                            tqp = _373.shift();
                        } else {
                            if (tqp.oper == "+") {
                                _375 = "/following-sibling::";
                                _376 = "[position()=1]";
                                tqp = _373.shift();
                            } else {
                                _375 = "//";
                            }
                        }
                    }
                    _372 += _375 + tqp.tag + _376;
                    if (tqp.id) {
                        _372 += "[@id='" + tqp.id + "'][1]";
                    }
                    d.forEach(tqp.classes, function (cn) {
                        var cnl = cn.length;
                        var _379 = " ";
                        if (cn.charAt(cnl - 1) == "*") {
                            _379 = "";
                            cn = cn.substr(0, cnl - 1);
                        }
                        _372 += "[contains(concat(' ',@class,' '), ' " + cn + _379 + "')]";
                    });
                    _369(_35c, tqp, function (_37a) {
                        return "[@" + _37a + "]";
                    }, function (_37b) {
                        _372 += _37b;
                    });
                }
                return _372;
            };
            var _37c = {};
            var _37d = function (path) {
                if (_37c[path]) {
                    return _37c[path];
                }
                var doc = d.doc;
                var _380 = _370(path);
                var tf = function (_382) {
                    var ret = [];
                    var _384;
                    var tdoc = doc;
                    if (_382) {
                        tdoc = (_382.nodeType == 9) ? _382 : _382.ownerDocument;
                    }
                    try {
                        _384 = tdoc.evaluate(_380, _382, null, XPathResult.ANY_TYPE, null);
                    } catch (e) {
                        console.debug("failure in exprssion:", _380, "under:", _382);
                        console.debug(e);
                    }
                    var _386 = _384.iterateNext();
                    while (_386) {
                        ret.push(_386);
                        _386 = _384.iterateNext();
                    }
                    return ret;
                };
                return _37c[path] = tf;
            };
            var _387 = {};
            var _388 = {};
            var _389 = function (_38a, _38b) {
                if (!_38a) {
                    return _38b;
                }
                if (!_38b) {
                    return _38a;
                }
                return function () {
                    return _38a.apply(window, arguments) && _38b.apply(window, arguments);
                };
            };
            var _38c = function (root) {
                var ret = [];
                var te, x = 0, tret = root[_33f];
                while ((te = tret[x++])) {
                    if (te.nodeType == 1) {
                        ret.push(te);
                    }
                }
                return ret;
            };
            var _392 = function (root, _394) {
                var ret = [];
                var te = root;
                while (te = te.nextSibling) {
                    if (te.nodeType == 1) {
                        ret.push(te);
                        if (_394) {
                            break;
                        }
                    }
                }
                return ret;
            };
            var _397 = function (_398, _399, _39a, idx) {
                var nidx = idx + 1;
                var _39d = (_399.length == nidx);
                var tqp = _399[idx];
                if (tqp.oper) {
                    var ecn = (tqp.oper == ">") ? _38c(_398) : _392(_398, (tqp.oper == "+"));
                    if (!ecn || !ecn.length) {
                        return;
                    }
                    nidx++;
                    _39d = (_399.length == nidx);
                    var tf = _3a1(_399[idx + 1]);
                    for (var x = 0, ecnl = ecn.length, te; x < ecnl, te = ecn[x]; x++) {
                        if (tf(te)) {
                            if (_39d) {
                                _39a.push(te);
                            } else {
                                _397(te, _399, _39a, nidx);
                            }
                        }
                    }
                }
                var _3a5 = _3a6(tqp)(_398);
                if (_39d) {
                    while (_3a5.length) {
                        _39a.push(_3a5.shift());
                    }
                } else {
                    while (_3a5.length) {
                        _397(_3a5.shift(), _399, _39a, nidx);
                    }
                }
            };
            var _3a7 = function (_3a8, _3a9) {
                var ret = [];
                var x = _3a8.length - 1, te;
                while ((te = _3a8[x--])) {
                    _397(te, _3a9, ret, 0);
                }
                return ret;
            };
            var _3a1 = function (q) {
                if (_387[q.query]) {
                    return _387[q.query];
                }
                var ff = null;
                if (q.tag) {
                    if (q.tag == "*") {
                        ff = _389(ff, function (elem) {
                            return (elem.nodeType == 1);
                        });
                    } else {
                        ff = _389(ff, function (elem) {
                            return ((elem.nodeType == 1) && (q[_340 ? "otag" : "tag"] == elem.tagName));
                        });
                    }
                }
                if (q.id) {
                    ff = _389(ff, function (elem) {
                        return ((elem.nodeType == 1) && (elem.id == q.id));
                    });
                }
                if (q.hasLoops) {
                    ff = _389(ff, _3b2(q));
                }
                return _387[q.query] = ff;
            };
            var _3b3 = function (node) {
                var pn = node.parentNode;
                var pnc = pn.childNodes;
                var nidx = -1;
                var _3b8 = pn.firstChild;
                if (!_3b8) {
                    return nidx;
                }
                var ci = node["__cachedIndex"];
                var cl = pn["__cachedLength"];
                if (((typeof cl == "number") && (cl != pnc.length)) || (typeof ci != "number")) {
                    pn["__cachedLength"] = pnc.length;
                    var idx = 1;
                    do {
                        if (_3b8 === node) {
                            nidx = idx;
                        }
                        if (_3b8.nodeType == 1) {
                            _3b8["__cachedIndex"] = idx;
                            idx++;
                        }
                        _3b8 = _3b8.nextSibling;
                    } while (_3b8);
                } else {
                    nidx = ci;
                }
                return nidx;
            };
            var _3bc = 0;
            var _3bd = "";
            var _3be = function (elem, attr) {
                if (attr == "class") {
                    return elem.className || _3bd;
                }
                if (attr == "for") {
                    return elem.htmlFor || _3bd;
                }
                if (attr == "style") {
                    return elem.style.cssText || _3bd;
                }
                return (_340 ? elem.getAttribute(attr) : elem.getAttribute(attr, 2)) || _3bd;
            };
            var _3c1 = {"*=": function (attr, _3c3) {
                return function (elem) {
                    return (_3be(elem, attr).indexOf(_3c3) >= 0);
                };
            }, "^=": function (attr, _3c6) {
                return function (elem) {
                    return (_3be(elem, attr).indexOf(_3c6) == 0);
                };
            }, "$=": function (attr, _3c9) {
                var tval = " " + _3c9;
                return function (elem) {
                    var ea = " " + _3be(elem, attr);
                    return (ea.lastIndexOf(_3c9) == (ea.length - _3c9.length));
                };
            }, "~=": function (attr, _3ce) {
                var tval = " " + _3ce + " ";
                return function (elem) {
                    var ea = " " + _3be(elem, attr) + " ";
                    return (ea.indexOf(tval) >= 0);
                };
            }, "|=": function (attr, _3d3) {
                var _3d4 = " " + _3d3 + "-";
                return function (elem) {
                    var ea = " " + (elem.getAttribute(attr, 2) || "");
                    return ((ea == _3d3) || (ea.indexOf(_3d4) == 0));
                };
            }, "=": function (attr, _3d8) {
                return function (elem) {
                    return (_3be(elem, attr) == _3d8);
                };
            }};
            var _3da = {"checked": function (name, _3dc) {
                return function (elem) {
                    return !!d.attr(elem, "checked");
                };
            }, "first-child": function (name, _3df) {
                return function (elem) {
                    if (elem.nodeType != 1) {
                        return false;
                    }
                    var fc = elem.previousSibling;
                    while (fc && (fc.nodeType != 1)) {
                        fc = fc.previousSibling;
                    }
                    return (!fc);
                };
            }, "last-child": function (name, _3e3) {
                return function (elem) {
                    if (elem.nodeType != 1) {
                        return false;
                    }
                    var nc = elem.nextSibling;
                    while (nc && (nc.nodeType != 1)) {
                        nc = nc.nextSibling;
                    }
                    return (!nc);
                };
            }, "empty": function (name, _3e7) {
                return function (elem) {
                    var cn = elem.childNodes;
                    var cnl = elem.childNodes.length;
                    for (var x = cnl - 1; x >= 0; x--) {
                        var nt = cn[x].nodeType;
                        if ((nt == 1) || (nt == 3)) {
                            return false;
                        }
                    }
                    return true;
                };
            }, "contains": function (name, _3ee) {
                return function (elem) {
                    if (_3ee.charAt(0) == "\"" || _3ee.charAt(0) == "'") {
                        _3ee = _3ee.substr(1, _3ee.length - 2);
                    }
                    return (elem.innerHTML.indexOf(_3ee) >= 0);
                };
            }, "not": function (name, _3f1) {
                var ntf = _3a1(_341(_3f1)[0]);
                return function (elem) {
                    return (!ntf(elem));
                };
            }, "nth-child": function (name, _3f5) {
                var pi = parseInt;
                if (_3f5 == "odd") {
                    _3f5 = "2n+1";
                } else {
                    if (_3f5 == "even") {
                        _3f5 = "2n";
                    }
                }
                if (_3f5.indexOf("n") != -1) {
                    var _3f7 = _3f5.split("n", 2);
                    var pred = _3f7[0] ? (_3f7[0] == "-" ? -1 : pi(_3f7[0])) : 1;
                    var idx = _3f7[1] ? pi(_3f7[1]) : 0;
                    var lb = 0, ub = -1;
                    if (pred > 0) {
                        if (idx < 0) {
                            idx = (idx % pred) && (pred + (idx % pred));
                        } else {
                            if (idx > 0) {
                                if (idx >= pred) {
                                    lb = idx - idx % pred;
                                }
                                idx = idx % pred;
                            }
                        }
                    } else {
                        if (pred < 0) {
                            pred *= -1;
                            if (idx > 0) {
                                ub = idx;
                                idx = idx % pred;
                            }
                        }
                    }
                    if (pred > 0) {
                        return function (elem) {
                            var i = _3b3(elem);
                            return (i >= lb) && (ub < 0 || i <= ub) && ((i % pred) == idx);
                        };
                    } else {
                        _3f5 = idx;
                    }
                }
                var _3fe = pi(_3f5);
                return function (elem) {
                    return (_3b3(elem) == _3fe);
                };
            }};
            var _400 = (d.isIE) ? function (cond) {
                var clc = cond.toLowerCase();
                return function (elem) {
                    return (_340 ? elem.getAttribute(cond) : elem[cond] || elem[clc]);
                };
            } : function (cond) {
                return function (elem) {
                    return (elem && elem.getAttribute && elem.hasAttribute(cond));
                };
            };
            var _3b2 = function (_406) {
                var _407 = (_388[_406.query] || _387[_406.query]);
                if (_407) {
                    return _407;
                }
                var ff = null;
                if (_406.id) {
                    if (_406.tag != "*") {
                        ff = _389(ff, function (elem) {
                            return (elem.tagName == _406[_340 ? "otag" : "tag"]);
                        });
                    }
                }
                d.forEach(_406.classes, function (_40a, idx, arr) {
                    var _40d = _40a.charAt(_40a.length - 1) == "*";
                    if (_40d) {
                        _40a = _40a.substr(0, _40a.length - 1);
                    }
                    var re = new RegExp("(?:^|\\s)" + _40a + (_40d ? ".*" : "") + "(?:\\s|$)");
                    ff = _389(ff, function (elem) {
                        return re.test(elem.className);
                    });
                    ff.count = idx;
                });
                d.forEach(_406.pseudos, function (_410) {
                    if (_3da[_410.name]) {
                        ff = _389(ff, _3da[_410.name](_410.name, _410.value));
                    }
                });
                _369(_3c1, _406, _400, function (_411) {
                    ff = _389(ff, _411);
                });
                if (!ff) {
                    ff = function () {
                        return true;
                    };
                }
                return _388[_406.query] = ff;
            };
            var _412 = {};
            var _3a6 = function (_413, root) {
                var fHit = _412[_413.query];
                if (fHit) {
                    return fHit;
                }
                if (_413.id && !_413.hasLoops && !_413.tag) {
                    return _412[_413.query] = function (root) {
                        return [d.byId(_413.id)];
                    };
                }
                var _417 = _3b2(_413);
                var _418;
                if (_413.tag && _413.id && !_413.hasLoops) {
                    _418 = function (root) {
                        var te = d.byId(_413.id, (root.ownerDocument || root));
                        if (_417(te)) {
                            return [te];
                        }
                    };
                } else {
                    var tret;
                    if (!_413.hasLoops) {
                        _418 = function (root) {
                            var ret = [];
                            var te, x = 0, tret = root.getElementsByTagName(_413[_340 ? "otag" : "tag"]);
                            while ((te = tret[x++])) {
                                ret.push(te);
                            }
                            return ret;
                        };
                    } else {
                        _418 = function (root) {
                            var ret = [];
                            var te, x = 0, tret = root.getElementsByTagName(_413[_340 ? "otag" : "tag"]);
                            while ((te = tret[x++])) {
                                if (_417(te)) {
                                    ret.push(te);
                                }
                            }
                            return ret;
                        };
                    }
                }
                return _412[_413.query] = _418;
            };
            var _424 = {};
            var _425 = {"*": d.isIE ? function (root) {
                return root.all;
            } : function (root) {
                return root.getElementsByTagName("*");
            }, "~": _392, "+": function (root) {
                return _392(root, true);
            }, ">": _38c};
            var _429 = function (_42a) {
                var _42b = _341(d.trim(_42a));
                if (_42b.length == 1) {
                    var tt = _3a6(_42b[0]);
                    tt.nozip = true;
                    return tt;
                }
                var sqf = function (root) {
                    var _42f = _42b.slice(0);
                    var _430;
                    if (_42f[0].oper == ">") {
                        _430 = [root];
                    } else {
                        _430 = _3a6(_42f.shift())(root);
                    }
                    return _3a7(_430, _42f);
                };
                return sqf;
            };
            var _431 = ((document["evaluate"] && !d.isSafari) ? function (_432, root) {
                var _434 = _432.split(" ");
                if ((!_340) && (document["evaluate"]) && (_432.indexOf(":") == -1) && (_432.indexOf("+") == -1)) {
                    if (((_434.length > 2) && (_432.indexOf(">") == -1)) || (_434.length > 3) || (_432.indexOf("[") >= 0) || ((1 == _434.length) && (0 <= _432.indexOf(".")))) {
                        return _37d(_432);
                    }
                }
                return _429(_432);
            } : _429);
            var _435 = function (_436) {
                if (_425[_436]) {
                    return _425[_436];
                }
                if (0 > _436.indexOf(",")) {
                    return _425[_436] = _431(_436);
                } else {
                    var _437 = _436.split(/\s*,\s*/);
                    var tf = function (root) {
                        var _43a = 0;
                        var ret = [];
                        var tp;
                        while ((tp = _437[_43a++])) {
                            ret = ret.concat(_431(tp, tp.indexOf(" "))(root));
                        }
                        return ret;
                    };
                    return _425[_436] = tf;
                }
            };
            var _43d = 0;
            var _zip = function (arr) {
                if (arr && arr.nozip) {
                    return d.NodeList._wrap(arr);
                }
                var ret = new d.NodeList();
                if (!arr) {
                    return ret;
                }
                if (arr[0]) {
                    ret.push(arr[0]);
                }
                if (arr.length < 2) {
                    return ret;
                }
                _43d++;
                if (d.isIE && _340) {
                    var _441 = _43d + "";
                    arr[0].setAttribute("_zipIdx", _441);
                    for (var x = 1, te; te = arr[x]; x++) {
                        if (arr[x].getAttribute("_zipIdx") != _441) {
                            ret.push(te);
                        }
                        te.setAttribute("_zipIdx", _441);
                    }
                } else {
                    arr[0]["_zipIdx"] = _43d;
                    for (var x = 1, te; te = arr[x]; x++) {
                        if (arr[x]["_zipIdx"] != _43d) {
                            ret.push(te);
                        }
                        te["_zipIdx"] = _43d;
                    }
                }
                return ret;
            };
            d.query = function (_444, root) {
                if (_444.constructor == d.NodeList) {
                    return _444;
                }
                if (!d.isString(_444)) {
                    return new d.NodeList(_444);
                }
                if (d.isString(root)) {
                    root = d.byId(root);
                }
                root = root || d.doc;
                var od = root.ownerDocument || root.documentElement;
                _340 = (root.contentType && root.contentType == "application/xml") || (!!od) && (d.isIE ? od.xml : (root.xmlVersion || od.xmlVersion));
                return _zip(_435(_444)(root));
            };
            d.query.pseudos = _3da;
            d._filterQueryResult = function (_447, _448) {
                var tnl = new d.NodeList();
                var ff = (_448) ? _3a1(_341(_448)[0]) : function () {
                    return true;
                };
                for (var x = 0, te; te = _447[x]; x++) {
                    if (ff(te)) {
                        tnl.push(te);
                    }
                }
                return tnl;
            };
        })();
    }
    if (!dojo._hasResource["dojo._base.xhr"]) {
        dojo._hasResource["dojo._base.xhr"] = true;
        dojo.provide("dojo._base.xhr");
        (function () {
            var _d = dojo;

            function setValue(obj, name, _450) {
                var val = obj[name];
                if (_d.isString(val)) {
                    obj[name] = [val, _450];
                } else {
                    if (_d.isArray(val)) {
                        val.push(_450);
                    } else {
                        obj[name] = _450;
                    }
                }
            };
            dojo.formToObject = function (_452) {
                var ret = {};
                var _454 = "file|submit|image|reset|button|";
                _d.forEach(dojo.byId(_452).elements, function (item) {
                    var _in = item.name;
                    var type = (item.type || "").toLowerCase();
                    if (_in && type && _454.indexOf(type) == -1 && !item.disabled) {
                        if (type == "radio" || type == "checkbox") {
                            if (item.checked) {
                                setValue(ret, _in, item.value);
                            }
                        } else {
                            if (item.multiple) {
                                ret[_in] = [];
                                _d.query("option", item).forEach(function (opt) {
                                    if (opt.selected) {
                                        setValue(ret, _in, opt.value);
                                    }
                                });
                            } else {
                                setValue(ret, _in, item.value);
                                if (type == "image") {
                                    ret[_in + ".x"] = ret[_in + ".y"] = ret[_in].x = ret[_in].y = 0;
                                }
                            }
                        }
                    }
                });
                return ret;
            };
            dojo.objectToQuery = function (map) {
                var enc = encodeURIComponent;
                var _45b = [];
                var _45c = {};
                for (var name in map) {
                    var _45e = map[name];
                    if (_45e != _45c[name]) {
                        var _45f = enc(name) + "=";
                        if (_d.isArray(_45e)) {
                            for (var i = 0; i < _45e.length; i++) {
                                _45b.push(_45f + enc(_45e[i]));
                            }
                        } else {
                            _45b.push(_45f + enc(_45e));
                        }
                    }
                }
                return _45b.join("&");
            };
            dojo.formToQuery = function (_461) {
                return _d.objectToQuery(_d.formToObject(_461));
            };
            dojo.formToJson = function (_462, _463) {
                return _d.toJson(_d.formToObject(_462), _463);
            };
            dojo.queryToObject = function (str) {
                var ret = {};
                var qp = str.split("&");
                var dec = decodeURIComponent;
                _d.forEach(qp, function (item) {
                    if (item.length) {
                        var _469 = item.split("=");
                        var name = dec(_469.shift());
                        var val = dec(_469.join("="));
                        if (_d.isString(ret[name])) {
                            ret[name] = [ret[name]];
                        }
                        if (_d.isArray(ret[name])) {
                            ret[name].push(val);
                        } else {
                            ret[name] = val;
                        }
                    }
                });
                return ret;
            };
            dojo._blockAsync = false;
            dojo._contentHandlers = {"text": function (xhr) {
                return xhr.responseText;
            }, "json": function (xhr) {
                return _d.fromJson(xhr.responseText || null);
            }, "json-comment-filtered": function (xhr) {
                if (!dojo.config.useCommentedJson) {
                    console.warn("Consider using the standard mimetype:application/json." + " json-commenting can introduce security issues. To" + " decrease the chances of hijacking, use the standard the 'json' handler and" + " prefix your json with: {}&&\n" + "Use djConfig.useCommentedJson=true to turn off this message.");
                }
                var _46f = xhr.responseText;
                var _470 = _46f.indexOf("/*");
                var _471 = _46f.lastIndexOf("*/");
                if (_470 == -1 || _471 == -1) {
                    throw new Error("JSON was not comment filtered");
                }
                return _d.fromJson(_46f.substring(_470 + 2, _471));
            }, "javascript": function (xhr) {
                return _d.eval(xhr.responseText);
            }, "xml": function (xhr) {
                var _474 = xhr.responseXML;
                if (_d.isIE && (!_474 || _474.documentElement == null)) {
                    _d.forEach(["MSXML2", "Microsoft", "MSXML", "MSXML3"], function (_475) {
                        try {
                            var dom = new ActiveXObject(_475 + ".XMLDOM");
                            dom.async = false;
                            dom.loadXML(xhr.responseText);
                            _474 = dom;
                        } catch (e) {
                        }
                    });
                }
                return _474;
            }};
            dojo._contentHandlers["json-comment-optional"] = function (xhr) {
                var _478 = _d._contentHandlers;
                if (xhr.responseText && xhr.responseText.indexOf("/*") != -1) {
                    return _478["json-comment-filtered"](xhr);
                } else {
                    return _478["json"](xhr);
                }
            };
            dojo._ioSetArgs = function (args, _47a, _47b, _47c) {
                var _47d = {args: args, url: args.url};
                var _47e = null;
                if (args.form) {
                    var form = _d.byId(args.form);
                    var _480 = form.getAttributeNode("action");
                    _47d.url = _47d.url || (_480 ? _480.value : null);
                    _47e = _d.formToObject(form);
                }
                var _481 = [
                    {}
                ];
                if (_47e) {
                    _481.push(_47e);
                }
                if (args.content) {
                    _481.push(args.content);
                }
                if (args.preventCache) {
                    _481.push({"dojo.preventCache": new Date().valueOf()});
                }
                _47d.query = _d.objectToQuery(_d.mixin.apply(null, _481));
                _47d.handleAs = args.handleAs || "text";
                var d = new _d.Deferred(_47a);
                d.addCallbacks(_47b, function (_483) {
                    return _47c(_483, d);
                });
                var ld = args.load;
                if (ld && _d.isFunction(ld)) {
                    d.addCallback(function (_485) {
                        return ld.call(args, _485, _47d);
                    });
                }
                var err = args.error;
                if (err && _d.isFunction(err)) {
                    d.addErrback(function (_487) {
                        return err.call(args, _487, _47d);
                    });
                }
                var _488 = args.handle;
                if (_488 && _d.isFunction(_488)) {
                    d.addBoth(function (_489) {
                        return _488.call(args, _489, _47d);
                    });
                }
                d.ioArgs = _47d;
                return d;
            };
            var _48a = function (dfd) {
                dfd.canceled = true;
                var xhr = dfd.ioArgs.xhr;
                var _at = typeof xhr.abort;
                if (_at == "function" || _at == "object" || _at == "unknown") {
                    xhr.abort();
                }
                var err = dfd.ioArgs.error;
                if (!err) {
                    err = new Error("xhr cancelled");
                    err.dojoType = "cancel";
                }
                return err;
            };
            var _48f = function (dfd) {
                var ret = _d._contentHandlers[dfd.ioArgs.handleAs](dfd.ioArgs.xhr);
                return (typeof ret == "undefined") ? null : ret;
            };
            var _492 = function (_493, dfd) {
                console.debug(_493);
                return _493;
            };
            var _495 = null;
            var _496 = [];
            var _497 = function () {
                var now = (new Date()).getTime();
                if (!_d._blockAsync) {
                    for (var i = 0, tif; i < _496.length && (tif = _496[i]); i++) {
                        var dfd = tif.dfd;
                        var func = function () {
                            if (!dfd || dfd.canceled || !tif.validCheck(dfd)) {
                                _496.splice(i--, 1);
                            } else {
                                if (tif.ioCheck(dfd)) {
                                    _496.splice(i--, 1);
                                    tif.resHandle(dfd);
                                } else {
                                    if (dfd.startTime) {
                                        if (dfd.startTime + (dfd.ioArgs.args.timeout || 0) < now) {
                                            _496.splice(i--, 1);
                                            var err = new Error("timeout exceeded");
                                            err.dojoType = "timeout";
                                            dfd.errback(err);
                                            dfd.cancel();
                                        }
                                    }
                                }
                            }
                        };
                        if (dojo.config.isDebug) {
                            func.call(this);
                        } else {
                            try {
                                func.call(this);
                            } catch (e) {
                                dfd.errback(e);
                            }
                        }
                    }
                }
                if (!_496.length) {
                    clearInterval(_495);
                    _495 = null;
                    return;
                }
            };
            dojo._ioCancelAll = function () {
                try {
                    _d.forEach(_496, function (i) {
                        try {
                            i.dfd.cancel();
                        } catch (e) {
                        }
                    });
                } catch (e) {
                }
            };
            if (_d.isIE) {
                _d.addOnWindowUnload(_d._ioCancelAll);
            }
            _d._ioWatch = function (dfd, _4a0, _4a1, _4a2) {
                if (dfd.ioArgs.args.timeout) {
                    dfd.startTime = (new Date()).getTime();
                }
                _496.push({dfd: dfd, validCheck: _4a0, ioCheck: _4a1, resHandle: _4a2});
                if (!_495) {
                    _495 = setInterval(_497, 50);
                }
                _497();
            };
            var _4a3 = "application/x-www-form-urlencoded";
            var _4a4 = function (dfd) {
                return dfd.ioArgs.xhr.readyState;
            };
            var _4a6 = function (dfd) {
                return 4 == dfd.ioArgs.xhr.readyState;
            };
            var _4a8 = function (dfd) {
                var xhr = dfd.ioArgs.xhr;
                if (_d._isDocumentOk(xhr)) {
                    dfd.callback(dfd);
                } else {
                    var err = new Error("Unable to load " + dfd.ioArgs.url + " status:" + xhr.status);
                    err.status = xhr.status;
                    err.responseText = xhr.responseText;
                    dfd.errback(err);
                }
            };
            dojo._ioAddQueryToUrl = function (_4ac) {
                if (_4ac.query.length) {
                    _4ac.url += (_4ac.url.indexOf("?") == -1 ? "?" : "&") + _4ac.query;
                    _4ac.query = null;
                }
            };
            dojo.xhr = function (_4ad, args, _4af) {
                var dfd = _d._ioSetArgs(args, _48a, _48f, _492);
                dfd.ioArgs.xhr = _d._xhrObj(dfd.ioArgs.args);
                if (_4af) {
                    if ("postData" in args) {
                        dfd.ioArgs.query = args.postData;
                    } else {
                        if ("putData" in args) {
                            dfd.ioArgs.query = args.putData;
                        }
                    }
                } else {
                    _d._ioAddQueryToUrl(dfd.ioArgs);
                }
                var _4b1 = dfd.ioArgs;
                var xhr = _4b1.xhr;
                xhr.open(_4ad, _4b1.url, args.sync !== true, args.user || undefined, args.password || undefined);
                if (args.headers) {
                    for (var hdr in args.headers) {
                        if (hdr.toLowerCase() === "content-type" && !args.contentType) {
                            args.contentType = args.headers[hdr];
                        } else {
                            xhr.setRequestHeader(hdr, args.headers[hdr]);
                        }
                    }
                }
                xhr.setRequestHeader("Content-Type", args.contentType || _4a3);
                if (!args.headers || !args.headers["X-Requested-With"]) {
                    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                }
                if (dojo.config.isDebug) {
                    xhr.send(_4b1.query);
                } else {
                    try {
                        xhr.send(_4b1.query);
                    } catch (e) {
                        dfd.ioArgs.error = e;
                        dfd.cancel();
                    }
                }
                _d._ioWatch(dfd, _4a4, _4a6, _4a8);
                xhr = null;
                return dfd;
            };
            dojo.xhrGet = function (args) {
                return _d.xhr("GET", args);
            };
            dojo.rawXhrPost = dojo.xhrPost = function (args) {
                return _d.xhr("POST", args, true);
            };
            dojo.rawXhrPut = dojo.xhrPut = function (args) {
                return _d.xhr("PUT", args, true);
            };
            dojo.xhrDelete = function (args) {
                return _d.xhr("DELETE", args);
            };
        })();
    }
    if (!dojo._hasResource["dojo._base.fx"]) {
        dojo._hasResource["dojo._base.fx"] = true;
        dojo.provide("dojo._base.fx");
        (function () {
            var d = dojo;
            dojo._Line = function (_4b9, end) {
                this.start = _4b9;
                this.end = end;
                this.getValue = function (n) {
                    return ((this.end - this.start) * n) + this.start;
                };
            };
            d.declare("dojo._Animation", null, {constructor: function (args) {
                d.mixin(this, args);
                if (d.isArray(this.curve)) {
                    this.curve = new d._Line(this.curve[0], this.curve[1]);
                }
            }, duration: 350, repeat: 0, rate: 10, _percent: 0, _startRepeatCount: 0, _fire: function (evt, args) {
                if (this[evt]) {
                    if (dojo.config.isDebug) {
                        this[evt].apply(this, args || []);
                    } else {
                        try {
                            this[evt].apply(this, args || []);
                        } catch (e) {
                            console.error("exception in animation handler for:", evt);
                            console.error(e);
                        }
                    }
                }
                return this;
            }, play: function (_4bf, _4c0) {
                var _t = this;
                if (_4c0) {
                    _t._stopTimer();
                    _t._active = _t._paused = false;
                    _t._percent = 0;
                } else {
                    if (_t._active && !_t._paused) {
                        return _t;
                    }
                }
                _t._fire("beforeBegin");
                var de = _4bf || _t.delay;
                var _p = dojo.hitch(_t, "_play", _4c0);
                if (de > 0) {
                    setTimeout(_p, de);
                    return _t;
                }
                _p();
                return _t;
            }, _play: function (_4c4) {
                var _t = this;
                _t._startTime = new Date().valueOf();
                if (_t._paused) {
                    _t._startTime -= _t.duration * _t._percent;
                }
                _t._endTime = _t._startTime + _t.duration;
                _t._active = true;
                _t._paused = false;
                var _4c6 = _t.curve.getValue(_t._percent);
                if (!_t._percent) {
                    if (!_t._startRepeatCount) {
                        _t._startRepeatCount = _t.repeat;
                    }
                    _t._fire("onBegin", [_4c6]);
                }
                _t._fire("onPlay", [_4c6]);
                _t._cycle();
                return _t;
            }, pause: function () {
                this._stopTimer();
                if (!this._active) {
                    return this;
                }
                this._paused = true;
                this._fire("onPause", [this.curve.getValue(this._percent)]);
                return this;
            }, gotoPercent: function (_4c7, _4c8) {
                this._stopTimer();
                this._active = this._paused = true;
                this._percent = _4c7;
                if (_4c8) {
                    this.play();
                }
                return this;
            }, stop: function (_4c9) {
                if (!this._timer) {
                    return this;
                }
                this._stopTimer();
                if (_4c9) {
                    this._percent = 1;
                }
                this._fire("onStop", [this.curve.getValue(this._percent)]);
                this._active = this._paused = false;
                return this;
            }, status: function () {
                if (this._active) {
                    return this._paused ? "paused" : "playing";
                }
                return "stopped";
            }, _cycle: function () {
                var _t = this;
                if (_t._active) {
                    var curr = new Date().valueOf();
                    var step = (curr - _t._startTime) / (_t._endTime - _t._startTime);
                    if (step >= 1) {
                        step = 1;
                    }
                    _t._percent = step;
                    if (_t.easing) {
                        step = _t.easing(step);
                    }
                    _t._fire("onAnimate", [_t.curve.getValue(step)]);
                    if (_t._percent < 1) {
                        _t._startTimer();
                    } else {
                        _t._active = false;
                        if (_t.repeat > 0) {
                            _t.repeat--;
                            _t.play(null, true);
                        } else {
                            if (_t.repeat == -1) {
                                _t.play(null, true);
                            } else {
                                if (_t._startRepeatCount) {
                                    _t.repeat = _t._startRepeatCount;
                                    _t._startRepeatCount = 0;
                                }
                            }
                        }
                        _t._percent = 0;
                        _t._fire("onEnd");
                        _t._stopTimer();
                    }
                }
                return _t;
            }});
            var ctr = 0;
            var _4ce = [];
            var _4cf = {run: function () {
            }};
            var _4d0 = null;
            dojo._Animation.prototype._startTimer = function () {
                if (!this._timer) {
                    this._timer = d.connect(_4cf, "run", this, "_cycle");
                    ctr++;
                }
                if (!_4d0) {
                    _4d0 = setInterval(d.hitch(_4cf, "run"), this.rate);
                }
            };
            dojo._Animation.prototype._stopTimer = function () {
                if (this._timer) {
                    d.disconnect(this._timer);
                    this._timer = null;
                    ctr--;
                }
                if (ctr <= 0) {
                    clearInterval(_4d0);
                    _4d0 = null;
                    ctr = 0;
                }
            };
            var _4d1 = (d.isIE) ? function (node) {
                var ns = node.style;
                if (!ns.width.length && d.style(node, "width") == "auto") {
                    ns.width = "auto";
                }
            } : function () {
            };
            dojo._fade = function (args) {
                args.node = d.byId(args.node);
                var _4d5 = d.mixin({properties: {}}, args);
                var _4d6 = (_4d5.properties.opacity = {});
                _4d6.start = !("start" in _4d5) ? function () {
                    return Number(d.style(_4d5.node, "opacity"));
                } : _4d5.start;
                _4d6.end = _4d5.end;
                var anim = d.animateProperty(_4d5);
                d.connect(anim, "beforeBegin", d.partial(_4d1, _4d5.node));
                return anim;
            };
            dojo.fadeIn = function (args) {
                return d._fade(d.mixin({end: 1}, args));
            };
            dojo.fadeOut = function (args) {
                return d._fade(d.mixin({end: 0}, args));
            };
            dojo._defaultEasing = function (n) {
                return 0.5 + ((Math.sin((n + 1.5) * Math.PI)) / 2);
            };
            var _4db = function (_4dc) {
                this._properties = _4dc;
                for (var p in _4dc) {
                    var prop = _4dc[p];
                    if (prop.start instanceof d.Color) {
                        prop.tempColor = new d.Color();
                    }
                }
                this.getValue = function (r) {
                    var ret = {};
                    for (var p in this._properties) {
                        var prop = this._properties[p];
                        var _4e3 = prop.start;
                        if (_4e3 instanceof d.Color) {
                            ret[p] = d.blendColors(_4e3, prop.end, r, prop.tempColor).toCss();
                        } else {
                            if (!d.isArray(_4e3)) {
                                ret[p] = ((prop.end - _4e3) * r) + _4e3 + (p != "opacity" ? prop.units || "px" : "");
                            }
                        }
                    }
                    return ret;
                };
            };
            dojo.animateProperty = function (args) {
                args.node = d.byId(args.node);
                if (!args.easing) {
                    args.easing = d._defaultEasing;
                }
                var anim = new d._Animation(args);
                d.connect(anim, "beforeBegin", anim, function () {
                    var pm = {};
                    for (var p in this.properties) {
                        if (p == "width" || p == "height") {
                            this.node.display = "block";
                        }
                        var prop = this.properties[p];
                        prop = pm[p] = d.mixin({}, (d.isObject(prop) ? prop : {end: prop}));
                        if (d.isFunction(prop.start)) {
                            prop.start = prop.start();
                        }
                        if (d.isFunction(prop.end)) {
                            prop.end = prop.end();
                        }
                        var _4e9 = (p.toLowerCase().indexOf("color") >= 0);

                        function getStyle(node, p) {
                            var v = ({height: node.offsetHeight, width: node.offsetWidth})[p];
                            if (v !== undefined) {
                                return v;
                            }
                            v = d.style(node, p);
                            return (p == "opacity") ? Number(v) : (_4e9 ? v : parseFloat(v));
                        };
                        if (!("end" in prop)) {
                            prop.end = getStyle(this.node, p);
                        } else {
                            if (!("start" in prop)) {
                                prop.start = getStyle(this.node, p);
                            }
                        }
                        if (_4e9) {
                            prop.start = new d.Color(prop.start);
                            prop.end = new d.Color(prop.end);
                        } else {
                            prop.start = (p == "opacity") ? Number(prop.start) : parseFloat(prop.start);
                        }
                    }
                    this.curve = new _4db(pm);
                });
                d.connect(anim, "onAnimate", d.hitch(d, "style", anim.node));
                return anim;
            };
            dojo.anim = function (node, _4ee, _4ef, _4f0, _4f1, _4f2) {
                return d.animateProperty({node: node, duration: _4ef || d._Animation.prototype.duration, properties: _4ee, easing: _4f0, onEnd: _4f1}).play(_4f2 || 0);
            };
        })();
    }
    if (!dojo._hasResource["dojo._base.browser"]) {
        dojo._hasResource["dojo._base.browser"] = true;
        dojo.provide("dojo._base.browser");
        if (dojo.config.require) {
            dojo.forEach(dojo.config.require, "dojo['require'](item);");
        }
    }
    if (!dojo._hasResource["dojo.i18n"]) {
        dojo._hasResource["dojo.i18n"] = true;
        dojo.provide("dojo.i18n");
        dojo.i18n.getLocalization = function (_4f3, _4f4, _4f5) {
            _4f5 = dojo.i18n.normalizeLocale(_4f5);
            var _4f6 = _4f5.split("-");
            var _4f7 = [_4f3, "nls", _4f4].join(".");
            var _4f8 = dojo._loadedModules[_4f7];
            if (_4f8) {
                var _4f9;
                for (var i = _4f6.length; i > 0; i--) {
                    var loc = _4f6.slice(0, i).join("_");
                    if (_4f8[loc]) {
                        _4f9 = _4f8[loc];
                        break;
                    }
                }
                if (!_4f9) {
                    _4f9 = _4f8.ROOT;
                }
                if (_4f9) {
                    var _4fc = function () {
                    };
                    _4fc.prototype = _4f9;
                    return new _4fc();
                }
            }
            throw new Error("Bundle not found: " + _4f4 + " in " + _4f3 + " , locale=" + _4f5);
        };
        dojo.i18n.normalizeLocale = function (_4fd) {
            var _4fe = _4fd ? _4fd.toLowerCase() : dojo.locale;
            if (_4fe == "root") {
                _4fe = "ROOT";
            }
            return _4fe;
        };
        dojo.i18n._requireLocalization = function (_4ff, _500, _501, _502) {
            var _503 = dojo.i18n.normalizeLocale(_501);
            var _504 = [_4ff, "nls", _500].join(".");
            var _505 = "";
            if (_502) {
                var _506 = _502.split(",");
                for (var i = 0; i < _506.length; i++) {
                    if (_503["indexOf"](_506[i]) == 0) {
                        if (_506[i].length > _505.length) {
                            _505 = _506[i];
                        }
                    }
                }
                if (!_505) {
                    _505 = "ROOT";
                }
            }
            var _508 = _502 ? _505 : _503;
            var _509 = dojo._loadedModules[_504];
            var _50a = null;
            if (_509) {
                if (dojo.config.localizationComplete && _509._built) {
                    return;
                }
                var _50b = _508.replace(/-/g, "_");
                var _50c = _504 + "." + _50b;
                _50a = dojo._loadedModules[_50c];
            }
            if (!_50a) {
                _509 = dojo["provide"](_504);
                var syms = dojo._getModuleSymbols(_4ff);
                var _50e = syms.concat("nls").join("/");
                var _50f;
                dojo.i18n._searchLocalePath(_508, _502, function (loc) {
                    var _511 = loc.replace(/-/g, "_");
                    var _512 = _504 + "." + _511;
                    var _513 = false;
                    if (!dojo._loadedModules[_512]) {
                        dojo["provide"](_512);
                        var _514 = [_50e];
                        if (loc != "ROOT") {
                            _514.push(loc);
                        }
                        _514.push(_500);
                        var _515 = _514.join("/") + ".js";
                        _513 = dojo._loadPath(_515, null, function (hash) {
                            var _517 = function () {
                            };
                            _517.prototype = _50f;
                            _509[_511] = new _517();
                            for (var j in hash) {
                                _509[_511][j] = hash[j];
                            }
                        });
                    } else {
                        _513 = true;
                    }
                    if (_513 && _509[_511]) {
                        _50f = _509[_511];
                    } else {
                        _509[_511] = _50f;
                    }
                    if (_502) {
                        return true;
                    }
                });
            }
            if (_502 && _503 != _505) {
                _509[_503.replace(/-/g, "_")] = _509[_505.replace(/-/g, "_")];
            }
        };
        (function () {
            var _519 = dojo.config.extraLocale;
            if (_519) {
                if (!_519 instanceof Array) {
                    _519 = [_519];
                }
                var req = dojo.i18n._requireLocalization;
                dojo.i18n._requireLocalization = function (m, b, _51d, _51e) {
                    req(m, b, _51d, _51e);
                    if (_51d) {
                        return;
                    }
                    for (var i = 0; i < _519.length; i++) {
                        req(m, b, _519[i], _51e);
                    }
                };
            }
        })();
        dojo.i18n._searchLocalePath = function (_520, down, _522) {
            _520 = dojo.i18n.normalizeLocale(_520);
            var _523 = _520.split("-");
            var _524 = [];
            for (var i = _523.length; i > 0; i--) {
                _524.push(_523.slice(0, i).join("-"));
            }
            _524.push(false);
            if (down) {
                _524.reverse();
            }
            for (var j = _524.length - 1; j >= 0; j--) {
                var loc = _524[j] || "ROOT";
                var stop = _522(loc);
                if (stop) {
                    break;
                }
            }
        };
        dojo.i18n._preloadLocalizations = function (_529, _52a) {
            function preload(_52b) {
                _52b = dojo.i18n.normalizeLocale(_52b);
                dojo.i18n._searchLocalePath(_52b, true, function (loc) {
                    for (var i = 0; i < _52a.length; i++) {
                        if (_52a[i] == loc) {
                            dojo["require"](_529 + "_" + loc);
                            return true;
                        }
                    }
                    return false;
                });
            };
            preload();
            var _52e = dojo.config.extraLocale || [];
            for (var i = 0; i < _52e.length; i++) {
                preload(_52e[i]);
            }
        };
    }
    if (!dojo._hasResource["dijit._base.focus"]) {
        dojo._hasResource["dijit._base.focus"] = true;
        dojo.provide("dijit._base.focus");
        dojo.mixin(dijit, {_curFocus: null, _prevFocus: null, isCollapsed: function () {
            var _530 = dojo.doc;
            if (_530.selection) {
                var s = _530.selection;
                if (s.type == "Text") {
                    return !s.createRange().htmlText.length;
                } else {
                    return !s.createRange().length;
                }
            } else {
                var _532 = dojo.global;
                var _533 = _532.getSelection();
                if (dojo.isString(_533)) {
                    return !_533;
                } else {
                    return _533.isCollapsed || !_533.toString();
                }
            }
        }, getBookmark: function () {
            var _534, _535 = dojo.doc.selection;
            if (_535) {
                var _536 = _535.createRange();
                if (_535.type.toUpperCase() == "CONTROL") {
                    if (_536.length) {
                        _534 = [];
                        var i = 0, len = _536.length;
                        while (i < len) {
                            _534.push(_536.item(i++));
                        }
                    } else {
                        _534 = null;
                    }
                } else {
                    _534 = _536.getBookmark();
                }
            } else {
                if (window.getSelection) {
                    _535 = dojo.global.getSelection();
                    if (_535) {
                        _536 = _535.getRangeAt(0);
                        _534 = _536.cloneRange();
                    }
                } else {
                    console.warn("No idea how to store the current selection for this browser!");
                }
            }
            return _534;
        }, moveToBookmark: function (_539) {
            var _53a = dojo.doc;
            if (_53a.selection) {
                var _53b;
                if (dojo.isArray(_539)) {
                    _53b = _53a.body.createControlRange();
                    dojo.forEach(_539, function (n) {
                        _53b.addElement(n);
                    });
                } else {
                    _53b = _53a.selection.createRange();
                    _53b.moveToBookmark(_539);
                }
                _53b.select();
            } else {
                var _53d = dojo.global.getSelection && dojo.global.getSelection();
                if (_53d && _53d.removeAllRanges) {
                    _53d.removeAllRanges();
                    _53d.addRange(_539);
                } else {
                    console.warn("No idea how to restore selection for this browser!");
                }
            }
        }, getFocus: function (menu, _53f) {
            return {node: menu && dojo.isDescendant(dijit._curFocus, menu.domNode) ? dijit._prevFocus : dijit._curFocus, bookmark: !dojo.withGlobal(_53f || dojo.global, dijit.isCollapsed) ? dojo.withGlobal(_53f || dojo.global, dijit.getBookmark) : null, openedForWindow: _53f};
        }, focus: function (_540) {
            if (!_540) {
                return;
            }
            var node = "node" in _540 ? _540.node : _540, _542 = _540.bookmark, _543 = _540.openedForWindow;
            if (node) {
                var _544 = (node.tagName.toLowerCase() == "iframe") ? node.contentWindow : node;
                if (_544 && _544.focus) {
                    try {
                        _544.focus();
                    } catch (e) {
                    }
                }
                dijit._onFocusNode(node);
            }
            if (_542 && dojo.withGlobal(_543 || dojo.global, dijit.isCollapsed)) {
                if (_543) {
                    _543.focus();
                }
                try {
                    dojo.withGlobal(_543 || dojo.global, dijit.moveToBookmark, null, [_542]);
                } catch (e) {
                }
            }
        }, _activeStack: [], registerWin: function (_545) {
            if (!_545) {
                _545 = window;
            }
            dojo.connect(_545.document, "onmousedown", function (evt) {
                dijit._justMouseDowned = true;
                setTimeout(function () {
                    dijit._justMouseDowned = false;
                }, 0);
                dijit._onTouchNode(evt.target || evt.srcElement);
            });
            var doc = _545.document;
            if (doc) {
                if (dojo.isIE) {
                    doc.attachEvent("onactivate", function (evt) {
                        if (evt.srcElement.tagName.toLowerCase() != "#document") {
                            dijit._onFocusNode(evt.srcElement);
                        }
                    });
                    doc.attachEvent("ondeactivate", function (evt) {
                        dijit._onBlurNode(evt.srcElement);
                    });
                } else {
                    doc.addEventListener("focus", function (evt) {
                        dijit._onFocusNode(evt.target);
                    }, true);
                    doc.addEventListener("blur", function (evt) {
                        dijit._onBlurNode(evt.target);
                    }, true);
                }
            }
            doc = null;
        }, _onBlurNode: function (node) {
            dijit._prevFocus = dijit._curFocus;
            dijit._curFocus = null;
            if (dijit._justMouseDowned) {
                return;
            }
            if (dijit._clearActiveWidgetsTimer) {
                clearTimeout(dijit._clearActiveWidgetsTimer);
            }
            dijit._clearActiveWidgetsTimer = setTimeout(function () {
                delete dijit._clearActiveWidgetsTimer;
                dijit._setStack([]);
                dijit._prevFocus = null;
            }, 100);
        }, _onTouchNode: function (node) {
            if (dijit._clearActiveWidgetsTimer) {
                clearTimeout(dijit._clearActiveWidgetsTimer);
                delete dijit._clearActiveWidgetsTimer;
            }
            var _54e = [];
            try {
                while (node) {
                    if (node.dijitPopupParent) {
                        node = dijit.byId(node.dijitPopupParent).domNode;
                    } else {
                        if (node.tagName && node.tagName.toLowerCase() == "body") {
                            if (node === dojo.body()) {
                                break;
                            }
                            node = dijit.getDocumentWindow(node.ownerDocument).frameElement;
                        } else {
                            var id = node.getAttribute && node.getAttribute("widgetId");
                            if (id) {
                                _54e.unshift(id);
                            }
                            node = node.parentNode;
                        }
                    }
                }
            } catch (e) {
            }
            dijit._setStack(_54e);
        }, _onFocusNode: function (node) {
            if (!node) {
                return;
            }
            if (node.nodeType == 9) {
                return;
            }
            if (node.nodeType == 9) {
                var _551 = dijit.getDocumentWindow(node).frameElement;
                if (!_551) {
                    return;
                }
                node = _551;
            }
            dijit._onTouchNode(node);
            if (node == dijit._curFocus) {
                return;
            }
            if (dijit._curFocus) {
                dijit._prevFocus = dijit._curFocus;
            }
            dijit._curFocus = node;
            dojo.publish("focusNode", [node]);
        }, _setStack: function (_552) {
            var _553 = dijit._activeStack;
            dijit._activeStack = _552;
            for (var _554 = 0; _554 < Math.min(_553.length, _552.length); _554++) {
                if (_553[_554] != _552[_554]) {
                    break;
                }
            }
            for (var i = _553.length - 1; i >= _554; i--) {
                var _556 = dijit.byId(_553[i]);
                if (_556) {
                    _556._focused = false;
                    _556._hasBeenBlurred = true;
                    if (_556._onBlur) {
                        _556._onBlur();
                    }
                    if (_556._setStateClass) {
                        _556._setStateClass();
                    }
                    dojo.publish("widgetBlur", [_556]);
                }
            }
            for (i = _554; i < _552.length; i++) {
                _556 = dijit.byId(_552[i]);
                if (_556) {
                    _556._focused = true;
                    if (_556._onFocus) {
                        _556._onFocus();
                    }
                    if (_556._setStateClass) {
                        _556._setStateClass();
                    }
                    dojo.publish("widgetFocus", [_556]);
                }
            }
        }});
        dojo.addOnLoad(dijit.registerWin);
    }
    if (!dojo._hasResource["dijit._base.manager"]) {
        dojo._hasResource["dijit._base.manager"] = true;
        dojo.provide("dijit._base.manager");
        dojo.declare("dijit.WidgetSet", null, {constructor: function () {
            this._hash = {};
        }, add: function (_557) {
            if (this._hash[_557.id]) {
                throw new Error("Tried to register widget with id==" + _557.id + " but that id is already registered");
            }
            this._hash[_557.id] = _557;
        }, remove: function (id) {
            delete this._hash[id];
        }, forEach: function (func) {
            for (var id in this._hash) {
                func(this._hash[id]);
            }
        }, filter: function (_55b) {
            var res = new dijit.WidgetSet();
            this.forEach(function (_55d) {
                if (_55b(_55d)) {
                    res.add(_55d);
                }
            });
            return res;
        }, byId: function (id) {
            return this._hash[id];
        }, byClass: function (cls) {
            return this.filter(function (_560) {
                return _560.declaredClass == cls;
            });
        }});
        dijit.registry = new dijit.WidgetSet();
        dijit._widgetTypeCtr = {};
        dijit.getUniqueId = function (_561) {
            var id;
            do {
                id = _561 + "_" + (_561 in dijit._widgetTypeCtr ? ++dijit._widgetTypeCtr[_561] : dijit._widgetTypeCtr[_561] = 0);
            } while (dijit.byId(id));
            return id;
        };
        if (dojo.isIE) {
            dojo.addOnWindowUnload(function () {
                dijit.registry.forEach(function (_563) {
                    _563.destroy();
                });
            });
        }
        dijit.byId = function (id) {
            return (dojo.isString(id)) ? dijit.registry.byId(id) : id;
        };
        dijit.byNode = function (node) {
            return dijit.registry.byId(node.getAttribute("widgetId"));
        };
        dijit.getEnclosingWidget = function (node) {
            while (node) {
                if (node.getAttribute && node.getAttribute("widgetId")) {
                    return dijit.registry.byId(node.getAttribute("widgetId"));
                }
                node = node.parentNode;
            }
            return null;
        };
        dijit._tabElements = {area: true, button: true, input: true, object: true, select: true, textarea: true};
        dijit._isElementShown = function (elem) {
            var _568 = dojo.style(elem);
            return (_568.visibility != "hidden") && (_568.visibility != "collapsed") && (_568.display != "none") && (dojo.attr(elem, "type") != "hidden");
        };
        dijit.isTabNavigable = function (elem) {
            if (dojo.hasAttr(elem, "disabled")) {
                return false;
            }
            var _56a = dojo.hasAttr(elem, "tabindex");
            var _56b = dojo.attr(elem, "tabindex");
            if (_56a && _56b >= 0) {
                return true;
            }
            var name = elem.nodeName.toLowerCase();
            if (((name == "a" && dojo.hasAttr(elem, "href")) || dijit._tabElements[name]) && (!_56a || _56b >= 0)) {
                return true;
            }
            return false;
        };
        dijit._getTabNavigable = function (root) {
            var _56e, last, _570, _571, _572, _573;
            var _574 = function (_575) {
                dojo.query("> *", _575).forEach(function (_576) {
                    var _577 = dijit._isElementShown(_576);
                    if (_577 && dijit.isTabNavigable(_576)) {
                        var _578 = dojo.attr(_576, "tabindex");
                        if (!dojo.hasAttr(_576, "tabindex") || _578 == 0) {
                            if (!_56e) {
                                _56e = _576;
                            }
                            last = _576;
                        } else {
                            if (_578 > 0) {
                                if (!_570 || _578 < _571) {
                                    _571 = _578;
                                    _570 = _576;
                                }
                                if (!_572 || _578 >= _573) {
                                    _573 = _578;
                                    _572 = _576;
                                }
                            }
                        }
                    }
                    if (_577 && _576.nodeName.toUpperCase() != "SELECT") {
                        _574(_576);
                    }
                });
            };
            if (dijit._isElementShown(root)) {
                _574(root);
            }
            return {first: _56e, last: last, lowest: _570, highest: _572};
        };
        dijit.getFirstInTabbingOrder = function (root) {
            var _57a = dijit._getTabNavigable(dojo.byId(root));
            return _57a.lowest ? _57a.lowest : _57a.first;
        };
        dijit.getLastInTabbingOrder = function (root) {
            var _57c = dijit._getTabNavigable(dojo.byId(root));
            return _57c.last ? _57c.last : _57c.highest;
        };
        dijit.defaultDuration = dojo.config["defaultDuration"] || 200;
    }
    if (!dojo._hasResource["dojo.AdapterRegistry"]) {
        dojo._hasResource["dojo.AdapterRegistry"] = true;
        dojo.provide("dojo.AdapterRegistry");
        dojo.AdapterRegistry = function (_57d) {
            this.pairs = [];
            this.returnWrappers = _57d || false;
        };
        dojo.extend(dojo.AdapterRegistry, {register: function (name, _57f, wrap, _581, _582) {
            this.pairs[((_582) ? "unshift" : "push")]([name, _57f, wrap, _581]);
        }, match: function () {
            for (var i = 0; i < this.pairs.length; i++) {
                var pair = this.pairs[i];
                if (pair[1].apply(this, arguments)) {
                    if ((pair[3]) || (this.returnWrappers)) {
                        return pair[2];
                    } else {
                        return pair[2].apply(this, arguments);
                    }
                }
            }
            throw new Error("No match found");
        }, unregister: function (name) {
            for (var i = 0; i < this.pairs.length; i++) {
                var pair = this.pairs[i];
                if (pair[0] == name) {
                    this.pairs.splice(i, 1);
                    return true;
                }
            }
            return false;
        }});
    }
    if (!dojo._hasResource["dijit._base.place"]) {
        dojo._hasResource["dijit._base.place"] = true;
        dojo.provide("dijit._base.place");
        dijit.getViewport = function () {
            var _588 = dojo.global;
            var _589 = dojo.doc;
            var w = 0, h = 0;
            var de = _589.documentElement;
            var dew = de.clientWidth, deh = de.clientHeight;
            if (dojo.isMozilla) {
                var minw, minh, maxw, maxh;
                var dbw = _589.body.clientWidth;
                if (dbw > dew) {
                    minw = dew;
                    maxw = dbw;
                } else {
                    maxw = dew;
                    minw = dbw;
                }
                var dbh = _589.body.clientHeight;
                if (dbh > deh) {
                    minh = deh;
                    maxh = dbh;
                } else {
                    maxh = deh;
                    minh = dbh;
                }
                w = (maxw > _588.innerWidth) ? minw : maxw;
                h = (maxh > _588.innerHeight) ? minh : maxh;
            } else {
                if (!dojo.isOpera && _588.innerWidth) {
                    w = _588.innerWidth;
                    h = _588.innerHeight;
                } else {
                    if (dojo.isIE && de && deh) {
                        w = dew;
                        h = deh;
                    } else {
                        if (dojo.body().clientWidth) {
                            w = dojo.body().clientWidth;
                            h = dojo.body().clientHeight;
                        }
                    }
                }
            }
            var _595 = dojo._docScroll();
            return {w: w, h: h, l: _595.x, t: _595.y};
        };
        dijit.placeOnScreen = function (node, pos, _598, _599) {
            var _59a = dojo.map(_598, function (_59b) {
                return {corner: _59b, pos: pos};
            });
            return dijit._place(node, _59a);
        };
        dijit._place = function (node, _59d, _59e) {
            var view = dijit.getViewport();
            if (!node.parentNode || String(node.parentNode.tagName).toLowerCase() != "body") {
                dojo.body().appendChild(node);
            }
            var best = null;
            dojo.some(_59d, function (_5a1) {
                var _5a2 = _5a1.corner;
                var pos = _5a1.pos;
                if (_59e) {
                    _59e(node, _5a1.aroundCorner, _5a2);
                }
                var _5a4 = node.style;
                var _5a5 = _5a4.display;
                var _5a6 = _5a4.visibility;
                _5a4.visibility = "hidden";
                _5a4.display = "";
                var mb = dojo.marginBox(node);
                _5a4.display = _5a5;
                _5a4.visibility = _5a6;
                var _5a8 = (_5a2.charAt(1) == "L" ? pos.x : Math.max(view.l, pos.x - mb.w)), _5a9 = (_5a2.charAt(0) == "T" ? pos.y : Math.max(view.t, pos.y - mb.h)), endX = (_5a2.charAt(1) == "L" ? Math.min(view.l + view.w, _5a8 + mb.w) : pos.x), endY = (_5a2.charAt(0) == "T" ? Math.min(view.t + view.h, _5a9 + mb.h) : pos.y), _5ac = endX - _5a8, _5ad = endY - _5a9, _5ae = (mb.w - _5ac) + (mb.h - _5ad);
                if (best == null || _5ae < best.overflow) {
                    best = {corner: _5a2, aroundCorner: _5a1.aroundCorner, x: _5a8, y: _5a9, w: _5ac, h: _5ad, overflow: _5ae};
                }
                return !_5ae;
            });
            node.style.left = best.x + "px";
            node.style.top = best.y + "px";
            if (best.overflow && _59e) {
                _59e(node, best.aroundCorner, best.corner);
            }
            return best;
        };
        dijit.placeOnScreenAroundNode = function (node, _5b0, _5b1, _5b2) {
            _5b0 = dojo.byId(_5b0);
            var _5b3 = _5b0.style.display;
            _5b0.style.display = "";
            var _5b4 = _5b0.offsetWidth;
            var _5b5 = _5b0.offsetHeight;
            var _5b6 = dojo.coords(_5b0, true);
            _5b0.style.display = _5b3;
            return dijit._placeOnScreenAroundRect(node, _5b6.x, _5b6.y, _5b4, _5b5, _5b1, _5b2);
        };
        dijit.placeOnScreenAroundRectangle = function (node, _5b8, _5b9, _5ba) {
            return dijit._placeOnScreenAroundRect(node, _5b8.x, _5b8.y, _5b8.width, _5b8.height, _5b9, _5ba);
        };
        dijit._placeOnScreenAroundRect = function (node, x, y, _5be, _5bf, _5c0, _5c1) {
            var _5c2 = [];
            for (var _5c3 in _5c0) {
                _5c2.push({aroundCorner: _5c3, corner: _5c0[_5c3], pos: {x: x + (_5c3.charAt(1) == "L" ? 0 : _5be), y: y + (_5c3.charAt(0) == "T" ? 0 : _5bf)}});
            }
            return dijit._place(node, _5c2, _5c1);
        };
        dijit.placementRegistry = new dojo.AdapterRegistry();
        dijit.placementRegistry.register("node", function (n, x) {
            return typeof x == "object" && typeof x.offsetWidth != "undefined" && typeof x.offsetHeight != "undefined";
        }, dijit.placeOnScreenAroundNode);
        dijit.placementRegistry.register("rect", function (n, x) {
            return typeof x == "object" && "x" in x && "y" in x && "width" in x && "height" in x;
        }, dijit.placeOnScreenAroundRectangle);
        dijit.placeOnScreenAroundElement = function (node, _5c9, _5ca, _5cb) {
            return dijit.placementRegistry.match.apply(dijit.placementRegistry, arguments);
        };
    }
    if (!dojo._hasResource["dijit._base.window"]) {
        dojo._hasResource["dijit._base.window"] = true;
        dojo.provide("dijit._base.window");
        dijit.getDocumentWindow = function (doc) {
            if (dojo.isIE && window !== document.parentWindow && !doc._parentWindow) {
                doc.parentWindow.execScript("document._parentWindow = window;", "Javascript");
                var win = doc._parentWindow;
                doc._parentWindow = null;
                return win;
            }
            return doc._parentWindow || doc.parentWindow || doc.defaultView;
        };
    }
    if (!dojo._hasResource["dijit._base.popup"]) {
        dojo._hasResource["dijit._base.popup"] = true;
        dojo.provide("dijit._base.popup");
        dijit.popup = new function () {
            var _5ce = [], _5cf = 1000, _5d0 = 1;
            this.prepare = function (node) {
                dojo.body().appendChild(node);
                var s = node.style;
                if (s.display == "none") {
                    s.display = "";
                }
                s.visibility = "hidden";
                s.position = "absolute";
                s.top = "-9999px";
            };
            this.open = function (args) {
                var _5d4 = args.popup, _5d5 = args.orient || {"BL": "TL", "TL": "BL"}, _5d6 = args.around, id = (args.around && args.around.id) ? (args.around.id + "_dropdown") : ("popup_" + _5d0++);
                var _5d8 = dojo.doc.createElement("div");
                dijit.setWaiRole(_5d8, "presentation");
                _5d8.id = id;
                _5d8.className = "dijitPopup";
                _5d8.style.zIndex = _5cf + _5ce.length;
                _5d8.style.left = _5d8.style.top = "0px";
                _5d8.style.visibility = "hidden";
                if (args.parent) {
                    _5d8.dijitPopupParent = args.parent.id;
                }
                dojo.body().appendChild(_5d8);
                var s = _5d4.domNode.style;
                s.display = "";
                s.visibility = "";
                s.position = "";
                _5d8.appendChild(_5d4.domNode);
                var _5da = new dijit.BackgroundIframe(_5d8);
                var best = _5d6 ? dijit.placeOnScreenAroundElement(_5d8, _5d6, _5d5, _5d4.orient ? dojo.hitch(_5d4, "orient") : null) : dijit.placeOnScreen(_5d8, args, _5d5 == "R" ? ["TR", "BR", "TL", "BL"] : ["TL", "BL", "TR", "BR"]);
                _5d8.style.visibility = "visible";
                var _5dc = [];
                var _5dd = function () {
                    for (var pi = _5ce.length - 1; pi > 0 && _5ce[pi].parent === _5ce[pi - 1].widget; pi--) {
                    }
                    return _5ce[pi];
                };
                _5dc.push(dojo.connect(_5d8, "onkeypress", this, function (evt) {
                    if (evt.charOrCode == dojo.keys.ESCAPE && args.onCancel) {
                        dojo.stopEvent(evt);
                        args.onCancel();
                    } else {
                        if (evt.charOrCode === dojo.keys.TAB) {
                            dojo.stopEvent(evt);
                            var _5e0 = _5dd();
                            if (_5e0 && _5e0.onCancel) {
                                _5e0.onCancel();
                            }
                        }
                    }
                }));
                if (_5d4.onCancel) {
                    _5dc.push(dojo.connect(_5d4, "onCancel", null, args.onCancel));
                }
                _5dc.push(dojo.connect(_5d4, _5d4.onExecute ? "onExecute" : "onChange", null, function () {
                    var _5e1 = _5dd();
                    if (_5e1 && _5e1.onExecute) {
                        _5e1.onExecute();
                    }
                }));
                _5ce.push({wrapper: _5d8, iframe: _5da, widget: _5d4, parent: args.parent, onExecute: args.onExecute, onCancel: args.onCancel, onClose: args.onClose, handlers: _5dc});
                if (_5d4.onOpen) {
                    _5d4.onOpen(best);
                }
                return best;
            };
            this.close = function (_5e2) {
                while (dojo.some(_5ce, function (elem) {
                    return elem.widget == _5e2;
                })) {
                    var top = _5ce.pop(), _5e5 = top.wrapper, _5e6 = top.iframe, _5e7 = top.widget, _5e8 = top.onClose;
                    if (_5e7.onClose) {
                        _5e7.onClose();
                    }
                    dojo.forEach(top.handlers, dojo.disconnect);
                    if (!_5e7 || !_5e7.domNode) {
                        return;
                    }
                    this.prepare(_5e7.domNode);
                    _5e6.destroy();
                    dojo._destroyElement(_5e5);
                    if (_5e8) {
                        _5e8();
                    }
                }
            };
        }();
        dijit._frames = new function () {
            var _5e9 = [];
            this.pop = function () {
                var _5ea;
                if (_5e9.length) {
                    _5ea = _5e9.pop();
                    _5ea.style.display = "";
                } else {
                    if (dojo.isIE) {
                        var burl = dojo.config["dojoBlankHtmlUrl"] || (dojo.moduleUrl("dojo", "resources/blank.html") + "") || "javascript:\"\"";
                        var html = "<iframe src='" + burl + "'" + " style='position: absolute; left: 0px; top: 0px;" + "z-index: -1; filter:Alpha(Opacity=\"0\");'>";
                        _5ea = dojo.doc.createElement(html);
                    } else {
                        _5ea = dojo.doc.createElement("iframe");
                        _5ea.src = "javascript:\"\"";
                        _5ea.className = "dijitBackgroundIframe";
                    }
                    _5ea.tabIndex = -1;
                    dojo.body().appendChild(_5ea);
                }
                return _5ea;
            };
            this.push = function (_5ed) {
                _5ed.style.display = "";
                if (dojo.isIE) {
                    _5ed.style.removeExpression("width");
                    _5ed.style.removeExpression("height");
                }
                _5e9.push(_5ed);
            };
        }();
        if (dojo.isIE < 7) {
            dojo.addOnLoad(function () {
                var f = dijit._frames;
                dojo.forEach([f.pop()], f.push);
            });
        }
        dijit.BackgroundIframe = function (node) {
            if (!node.id) {
                throw new Error("no id");
            }
            if ((dojo.isIE && dojo.isIE < 7) || (dojo.isFF && dojo.isFF < 3 && dojo.hasClass(dojo.body(), "dijit_a11y"))) {
                var _5f0 = dijit._frames.pop();
                node.appendChild(_5f0);
                if (dojo.isIE) {
                    _5f0.style.setExpression("width", dojo._scopeName + ".doc.getElementById('" + node.id + "').offsetWidth");
                    _5f0.style.setExpression("height", dojo._scopeName + ".doc.getElementById('" + node.id + "').offsetHeight");
                }
                this.iframe = _5f0;
            }
        };
        dojo.extend(dijit.BackgroundIframe, {destroy: function () {
            if (this.iframe) {
                dijit._frames.push(this.iframe);
                delete this.iframe;
            }
        }});
    }
    if (!dojo._hasResource["dijit._base.scroll"]) {
        dojo._hasResource["dijit._base.scroll"] = true;
        dojo.provide("dijit._base.scroll");
        dijit.scrollIntoView = function (node) {
            node = dojo.byId(node);
            var body = node.ownerDocument.body;
            var html = body.parentNode;
            if (dojo.isFF == 2 || node == body || node == html) {
                node.scrollIntoView(false);
                return;
            }
            var rtl = !dojo._isBodyLtr();
            var _5f5 = dojo.doc.compatMode != "BackCompat";
            var _5f6 = (_5f5 && !dojo.isSafari) ? html : body;

            function addPseudoAttrs(_5f7) {
                var _5f8 = _5f7.parentNode;
                var _5f9 = _5f7.offsetParent;
                if (_5f9 == null) {
                    _5f7 = _5f6;
                    _5f9 = html;
                    _5f8 = null;
                }
                _5f7._offsetParent = (_5f9 == body) ? _5f6 : _5f9;
                _5f7._parent = (_5f8 == body) ? _5f6 : _5f8;
                _5f7._start = {H: _5f7.offsetLeft, V: _5f7.offsetTop};
                _5f7._scroll = {H: _5f7.scrollLeft, V: _5f7.scrollTop};
                _5f7._renderedSize = {H: _5f7.offsetWidth, V: _5f7.offsetHeight};
                var bp = dojo._getBorderExtents(_5f7);
                _5f7._borderStart = {H: bp.l, V: bp.t};
                _5f7._borderSize = {H: bp.w, V: bp.h};
                _5f7._clientSize = (_5f7._offsetParent == html && dojo.isSafari && _5f5) ? {H: html.clientWidth, V: html.clientHeight} : {H: _5f7.clientWidth, V: _5f7.clientHeight};
                _5f7._scrollBarSize = {V: null, H: null};
                for (var dir in _5f7._scrollBarSize) {
                    var _5fc = _5f7._renderedSize[dir] - _5f7._clientSize[dir] - _5f7._borderSize[dir];
                    _5f7._scrollBarSize[dir] = (_5f7._clientSize[dir] > 0 && _5fc >= 15 && _5fc <= 17) ? _5fc : 0;
                }
                _5f7._isScrollable = {V: null, H: null};
                for (dir in _5f7._isScrollable) {
                    var _5fd = dir == "H" ? "V" : "H";
                    _5f7._isScrollable[dir] = _5f7 == _5f6 || _5f7._scroll[dir] || _5f7._scrollBarSize[_5fd];
                }
            };
            var _5fe = node;
            while (_5fe != null) {
                addPseudoAttrs(_5fe);
                var next = _5fe._parent;
                if (next) {
                    next._child = _5fe;
                }
                _5fe = next;
            }
            for (var dir in _5f6._renderedSize) {
                _5f6._renderedSize[dir] = Math.min(_5f6._clientSize[dir], _5f6._renderedSize[dir]);
            }
            var _601 = node;
            while (_601 != _5f6) {
                _5fe = _601._parent;
                if (_5fe.tagName == "TD") {
                    var _602 = _5fe._parent._parent._parent;
                    if (_602._offsetParent == _601._offsetParent && _5fe._offsetParent != _601._offsetParent) {
                        _5fe = _602;
                    }
                }
                var _603 = _601 == _5f6 || (_5fe._offsetParent != _601._offsetParent);
                for (dir in _601._start) {
                    var _604 = dir == "H" ? "V" : "H";
                    if (rtl && dir == "H" && (dojo.isSafari || dojo.isIE) && _5fe._clientSize.H > 0) {
                        var _605 = _5fe.scrollWidth - _5fe._clientSize.H;
                        if (_605 > 0) {
                            _5fe._scroll.H -= _605;
                        }
                    }
                    if (dojo.isIE && _5fe._offsetParent.tagName == "TABLE") {
                        _5fe._start[dir] -= _5fe._offsetParent._borderStart[dir];
                        _5fe._borderStart[dir] = _5fe._borderSize[dir] = 0;
                    }
                    if (_5fe._clientSize[dir] == 0) {
                        _5fe._renderedSize[dir] = _5fe._clientSize[dir] = _5fe._child._clientSize[dir];
                        if (rtl && dir == "H") {
                            _5fe._start[dir] -= _5fe._renderedSize[dir];
                        }
                    } else {
                        _5fe._renderedSize[dir] -= _5fe._borderSize[dir] + _5fe._scrollBarSize[dir];
                    }
                    _5fe._start[dir] += _5fe._borderStart[dir];
                    var _606 = _601._start[dir] - (_603 ? 0 : _5fe._start[dir]) - _5fe._scroll[dir];
                    var _607 = _606 + _601._renderedSize[dir] - _5fe._renderedSize[dir];
                    var _608, _609 = (dir == "H") ? "scrollLeft" : "scrollTop";
                    var _60a = (dir == "H" && rtl);
                    var _60b = _60a ? -_607 : _606;
                    var _60c = _60a ? -_606 : _607;
                    if (_60b <= 0) {
                        _608 = _60b;
                    } else {
                        if (_60c <= 0) {
                            _608 = 0;
                        } else {
                            if (_60b < _60c) {
                                _608 = _60b;
                            } else {
                                _608 = _60c;
                            }
                        }
                    }
                    var _60d = 0;
                    if (_608 != 0) {
                        var _60e = _5fe[_609];
                        _5fe[_609] += _60a ? -_608 : _608;
                        _60d = _5fe[_609] - _60e;
                        _606 -= _60d;
                        _60c -= _60a ? -_60d : _60d;
                    }
                    _5fe._renderedSize[dir] = _601._renderedSize[dir] + _5fe._scrollBarSize[dir] - ((_5fe._isScrollable[dir] && _60c > 0) ? _60c : 0);
                    _5fe._start[dir] += (_606 >= 0 || !_5fe._isScrollable[dir]) ? _606 : 0;
                }
                _601 = _5fe;
            }
        };
    }
    if (!dojo._hasResource["dijit._base.sniff"]) {
        dojo._hasResource["dijit._base.sniff"] = true;
        dojo.provide("dijit._base.sniff");
        (function () {
            var d = dojo;
            var ie = d.isIE;
            var _611 = d.isOpera;
            var maj = Math.floor;
            var ff = d.isFF;
            var _614 = d.boxModel.replace(/-/, "");
            var _615 = {dj_ie: ie, dj_ie6: maj(ie) == 6, dj_ie7: maj(ie) == 7, dj_iequirks: ie && d.isQuirks, dj_opera: _611, dj_opera8: maj(_611) == 8, dj_opera9: maj(_611) == 9, dj_khtml: d.isKhtml, dj_safari: d.isSafari, dj_gecko: d.isMozilla, dj_ff2: maj(ff) == 2, dj_ff3: maj(ff) == 3};
            _615["dj_" + _614] = true;
            var html = dojo.doc.documentElement;
            for (var p in _615) {
                if (_615[p]) {
                    if (html.className) {
                        html.className += " " + p;
                    } else {
                        html.className = p;
                    }
                }
            }
            dojo._loaders.unshift(function () {
                if (!dojo._isBodyLtr()) {
                    html.className += " dijitRtl";
                    for (var p in _615) {
                        if (_615[p]) {
                            html.className += " " + p + "-rtl";
                        }
                    }
                }
            });
        })();
    }
    if (!dojo._hasResource["dijit._base.typematic"]) {
        dojo._hasResource["dijit._base.typematic"] = true;
        dojo.provide("dijit._base.typematic");
        dijit.typematic = {_fireEventAndReload: function () {
            this._timer = null;
            this._callback(++this._count, this._node, this._evt);
            this._currentTimeout = (this._currentTimeout < 0) ? this._initialDelay : ((this._subsequentDelay > 1) ? this._subsequentDelay : Math.round(this._currentTimeout * this._subsequentDelay));
            this._timer = setTimeout(dojo.hitch(this, "_fireEventAndReload"), this._currentTimeout);
        }, trigger: function (evt, _61a, node, _61c, obj, _61e, _61f) {
            if (obj != this._obj) {
                this.stop();
                this._initialDelay = _61f || 500;
                this._subsequentDelay = _61e || 0.9;
                this._obj = obj;
                this._evt = evt;
                this._node = node;
                this._currentTimeout = -1;
                this._count = -1;
                this._callback = dojo.hitch(_61a, _61c);
                this._fireEventAndReload();
            }
        }, stop: function () {
            if (this._timer) {
                clearTimeout(this._timer);
                this._timer = null;
            }
            if (this._obj) {
                this._callback(-1, this._node, this._evt);
                this._obj = null;
            }
        }, addKeyListener: function (node, _621, _622, _623, _624, _625) {
            if (_621.keyCode) {
                _621.charOrCode = _621.keyCode;
                dojo.deprecated("keyCode attribute parameter for dijit.typematic.addKeyListener is deprecated. Use charOrCode instead.", "", "2.0");
            } else {
                if (_621.charCode) {
                    _621.charOrCode = String.fromCharCode(_621.charCode);
                    dojo.deprecated("charCode attribute parameter for dijit.typematic.addKeyListener is deprecated. Use charOrCode instead.", "", "2.0");
                }
            }
            return [dojo.connect(node, "onkeypress", this, function (evt) {
                if (evt.charOrCode == _621.charOrCode && (_621.ctrlKey === undefined || _621.ctrlKey == evt.ctrlKey) && (_621.altKey === undefined || _621.altKey == evt.ctrlKey) && (_621.shiftKey === undefined || _621.shiftKey == evt.ctrlKey)) {
                    dojo.stopEvent(evt);
                    dijit.typematic.trigger(_621, _622, node, _623, _621, _624, _625);
                } else {
                    if (dijit.typematic._obj == _621) {
                        dijit.typematic.stop();
                    }
                }
            }), dojo.connect(node, "onkeyup", this, function (evt) {
                if (dijit.typematic._obj == _621) {
                    dijit.typematic.stop();
                }
            })];
        }, addMouseListener: function (node, _629, _62a, _62b, _62c) {
            var dc = dojo.connect;
            return [dc(node, "mousedown", this, function (evt) {
                dojo.stopEvent(evt);
                dijit.typematic.trigger(evt, _629, node, _62a, node, _62b, _62c);
            }), dc(node, "mouseup", this, function (evt) {
                dojo.stopEvent(evt);
                dijit.typematic.stop();
            }), dc(node, "mouseout", this, function (evt) {
                dojo.stopEvent(evt);
                dijit.typematic.stop();
            }), dc(node, "mousemove", this, function (evt) {
                dojo.stopEvent(evt);
            }), dc(node, "dblclick", this, function (evt) {
                dojo.stopEvent(evt);
                if (dojo.isIE) {
                    dijit.typematic.trigger(evt, _629, node, _62a, node, _62b, _62c);
                    setTimeout(dojo.hitch(this, dijit.typematic.stop), 50);
                }
            })];
        }, addListener: function (_633, _634, _635, _636, _637, _638, _639) {
            return this.addKeyListener(_634, _635, _636, _637, _638, _639).concat(this.addMouseListener(_633, _636, _637, _638, _639));
        }};
    }
    if (!dojo._hasResource["dijit._base.wai"]) {
        dojo._hasResource["dijit._base.wai"] = true;
        dojo.provide("dijit._base.wai");
        dijit.wai = {onload: function () {
            var div = dojo.doc.createElement("div");
            div.id = "a11yTestNode";
            div.style.cssText = "border: 1px solid;" + "border-color:red green;" + "position: absolute;" + "height: 5px;" + "top: -999px;" + "background-image: url(\"" + (dojo.config.blankGif || dojo.moduleUrl("dojo", "resources/blank.gif")) + "\");";
            dojo.body().appendChild(div);
            var cs = dojo.getComputedStyle(div);
            if (cs) {
                var _63c = cs.backgroundImage;
                var _63d = (cs.borderTopColor == cs.borderRightColor) || (_63c != null && (_63c == "none" || _63c == "url(invalid-url:)"));
                dojo[_63d ? "addClass" : "removeClass"](dojo.body(), "dijit_a11y");
                if (dojo.isIE) {
                    div.outerHTML = "";
                } else {
                    dojo.body().removeChild(div);
                }
            }
        }};
        if (dojo.isIE || dojo.isMoz) {
            dojo._loaders.unshift(dijit.wai.onload);
        }
        dojo.mixin(dijit, {_XhtmlRoles: /banner|contentinfo|definition|main|navigation|search|note|secondary|seealso/, hasWaiRole: function (elem, role) {
            var _640 = this.getWaiRole(elem);
            if (role) {
                return (_640.indexOf(role) > -1);
            } else {
                return (_640.length > 0);
            }
        }, getWaiRole: function (elem) {
            return dojo.trim((dojo.attr(elem, "role") || "").replace(this._XhtmlRoles, "").replace("wairole:", ""));
        }, setWaiRole: function (elem, role) {
            var _644 = dojo.attr(elem, "role") || "";
            if (dojo.isFF < 3 || !this._XhtmlRoles.test(_644)) {
                dojo.attr(elem, "role", dojo.isFF < 3 ? "wairole:" + role : role);
            } else {
                if ((" " + _644 + " ").indexOf(" " + role + " ") < 0) {
                    var _645 = dojo.trim(_644.replace(this._XhtmlRoles, ""));
                    var _646 = dojo.trim(_644.replace(_645, ""));
                    dojo.attr(elem, "role", _646 + (_646 ? " " : "") + role);
                }
            }
        }, removeWaiRole: function (elem, role) {
            var _649 = dojo.attr(elem, "role");
            if (!_649) {
                return;
            }
            if (role) {
                var _64a = dojo.isFF < 3 ? "wairole:" + role : role;
                var t = dojo.trim((" " + _649 + " ").replace(" " + _64a + " ", " "));
                dojo.attr(elem, "role", t);
            } else {
                elem.removeAttribute("role");
            }
        }, hasWaiState: function (elem, _64d) {
            if (dojo.isFF < 3) {
                return elem.hasAttributeNS("http://www.w3.org/2005/07/aaa", _64d);
            } else {
                return elem.hasAttribute ? elem.hasAttribute("aria-" + _64d) : !!elem.getAttribute("aria-" + _64d);
            }
        }, getWaiState: function (elem, _64f) {
            if (dojo.isFF < 3) {
                return elem.getAttributeNS("http://www.w3.org/2005/07/aaa", _64f);
            } else {
                var _650 = elem.getAttribute("aria-" + _64f);
                return _650 ? _650 : "";
            }
        }, setWaiState: function (elem, _652, _653) {
            if (dojo.isFF < 3) {
                elem.setAttributeNS("http://www.w3.org/2005/07/aaa", "aaa:" + _652, _653);
            } else {
                elem.setAttribute("aria-" + _652, _653);
            }
        }, removeWaiState: function (elem, _655) {
            if (dojo.isFF < 3) {
                elem.removeAttributeNS("http://www.w3.org/2005/07/aaa", _655);
            } else {
                elem.removeAttribute("aria-" + _655);
            }
        }});
    }
    if (!dojo._hasResource["dijit._base"]) {
        dojo._hasResource["dijit._base"] = true;
        dojo.provide("dijit._base");
    }
    if (!dojo._hasResource["dijit._Widget"]) {
        dojo._hasResource["dijit._Widget"] = true;
        dojo.provide("dijit._Widget");
        dojo.require("dijit._base");
        dojo.connect(dojo, "connect", function (_656, _657) {
            if (_656 && dojo.isFunction(_656._onConnect)) {
                _656._onConnect(_657);
            }
        });
        dijit._connectOnUseEventHandler = function (_658) {
        };
        (function () {
            var _659 = {};
            var _65a = function (dc) {
                if (!_659[dc]) {
                    var r = [];
                    var _65d;
                    var _65e = dojo.getObject(dc).prototype;
                    for (var _65f in _65e) {
                        if (dojo.isFunction(_65e[_65f]) && (_65d = _65f.match(/^_set([a-zA-Z]*)Attr$/)) && _65d[1]) {
                            r.push(_65d[1].charAt(0).toLowerCase() + _65d[1].substr(1));
                        }
                    }
                    _659[dc] = r;
                }
                return _659[dc] || [];
            };
            dojo.declare("dijit._Widget", null, {id: "", lang: "", dir: "", "class": "", style: "", title: "", srcNodeRef: null, domNode: null, containerNode: null, attributeMap: {id: "", dir: "", lang: "", "class": "", style: "", title: ""}, _deferredConnects: {onClick: "", onDblClick: "", onKeyDown: "", onKeyPress: "", onKeyUp: "", onMouseMove: "", onMouseDown: "", onMouseOut: "", onMouseOver: "", onMouseLeave: "", onMouseEnter: "", onMouseUp: ""}, onClick: dijit._connectOnUseEventHandler, onDblClick: dijit._connectOnUseEventHandler, onKeyDown: dijit._connectOnUseEventHandler, onKeyPress: dijit._connectOnUseEventHandler, onKeyUp: dijit._connectOnUseEventHandler, onMouseDown: dijit._connectOnUseEventHandler, onMouseMove: dijit._connectOnUseEventHandler, onMouseOut: dijit._connectOnUseEventHandler, onMouseOver: dijit._connectOnUseEventHandler, onMouseLeave: dijit._connectOnUseEventHandler, onMouseEnter: dijit._connectOnUseEventHandler, onMouseUp: dijit._connectOnUseEventHandler, _blankGif: (dojo.config.blankGif || dojo.moduleUrl("dojo", "resources/blank.gif")), postscript: function (_660, _661) {
                this.create(_660, _661);
            }, create: function (_662, _663) {
                this.srcNodeRef = dojo.byId(_663);
                this._connects = [];
                this._deferredConnects = dojo.clone(this._deferredConnects);
                for (var attr in this.attributeMap) {
                    delete this._deferredConnects[attr];
                }
                for (attr in this._deferredConnects) {
                    if (this[attr] !== dijit._connectOnUseEventHandler) {
                        delete this._deferredConnects[attr];
                    }
                }
                if (this.srcNodeRef && (typeof this.srcNodeRef.id == "string")) {
                    this.id = this.srcNodeRef.id;
                }
                if (_662) {
                    this.params = _662;
                    dojo.mixin(this, _662);
                }
                this.postMixInProperties();
                if (!this.id) {
                    this.id = dijit.getUniqueId(this.declaredClass.replace(/\./g, "_"));
                }
                dijit.registry.add(this);
                this.buildRendering();
                if (this.domNode) {
                    this._applyAttributes();
                    for (attr in this.params) {
                        this._onConnect(attr);
                    }
                }
                if (this.domNode) {
                    this.domNode.setAttribute("widgetId", this.id);
                }
                this.postCreate();
                if (this.srcNodeRef && !this.srcNodeRef.parentNode) {
                    delete this.srcNodeRef;
                }
                this._created = true;
            }, _applyAttributes: function () {
                var _665 = function (attr, _667) {
                    if ((_667.params && attr in _667.params) || _667[attr]) {
                        _667.attr(attr, _667[attr]);
                    }
                };
                for (var attr in this.attributeMap) {
                    _665(attr, this);
                }
                dojo.forEach(_65a(this.declaredClass), function (a) {
                    if (!(a in this.attributeMap)) {
                        _665(a, this);
                    }
                }, this);
            }, postMixInProperties: function () {
            }, buildRendering: function () {
                this.domNode = this.srcNodeRef || dojo.doc.createElement("div");
            }, postCreate: function () {
            }, startup: function () {
                this._started = true;
            }, destroyRecursive: function (_66a) {
                this.destroyDescendants(_66a);
                this.destroy(_66a);
            }, destroy: function (_66b) {
                this.uninitialize();
                dojo.forEach(this._connects, function (_66c) {
                    dojo.forEach(_66c, dojo.disconnect);
                });
                dojo.forEach(this._supportingWidgets || [], function (w) {
                    if (w.destroy) {
                        w.destroy();
                    }
                });
                this.destroyRendering(_66b);
                dijit.registry.remove(this.id);
            }, destroyRendering: function (_66e) {
                if (this.bgIframe) {
                    this.bgIframe.destroy(_66e);
                    delete this.bgIframe;
                }
                if (this.domNode) {
                    if (!_66e) {
                        dojo._destroyElement(this.domNode);
                    }
                    delete this.domNode;
                }
                if (this.srcNodeRef) {
                    if (!_66e) {
                        dojo._destroyElement(this.srcNodeRef);
                    }
                    delete this.srcNodeRef;
                }
            }, destroyDescendants: function (_66f) {
                dojo.forEach(this.getDescendants(), function (_670) {
                    if (_670.destroy) {
                        _670.destroy(_66f);
                    }
                });
            }, uninitialize: function () {
                return false;
            }, onFocus: function () {
            }, onBlur: function () {
            }, _onFocus: function (e) {
                this.onFocus();
            }, _onBlur: function () {
                this.onBlur();
            }, _onConnect: function (_672) {
                if (_672 in this._deferredConnects) {
                    var _673 = this[this._deferredConnects[_672] || "domNode"];
                    this.connect(_673, _672.toLowerCase(), this[_672]);
                    delete this._deferredConnects[_672];
                }
            }, _setClassAttr: function (_674) {
                var _675 = this[this.attributeMap["class"] || "domNode"];
                dojo.removeClass(_675, this["class"]);
                this["class"] = _674;
                dojo.addClass(_675, _674);
            }, _setStyleAttr: function (_676) {
                var _677 = this[this.attributeMap["style"] || "domNode"];
                if (_677.style.cssText) {
                    _677.style.cssText += "; " + _676;
                } else {
                    _677.style.cssText = _676;
                }
                this["style"] = _676;
            }, setAttribute: function (attr, _679) {
                dojo.deprecated(this.declaredClass + "::setAttribute() is deprecated. Use attr() instead.", "", "2.0");
                this.attr(attr, _679);
            }, _attrToDom: function (attr, _67b) {
                var _67c = this.attributeMap[attr];
                dojo.forEach(dojo.isArray(_67c) ? _67c : [_67c], function (_67d) {
                    var _67e = this[_67d.node || _67d || "domNode"];
                    var type = _67d.type || "attribute";
                    switch (type) {
                        case "attribute":
                            if (dojo.isFunction(_67b)) {
                                _67b = dojo.hitch(this, _67b);
                            }
                            if (/^on[A-Z][a-zA-Z]*$/.test(attr)) {
                                attr = attr.toLowerCase();
                            }
                            dojo.attr(_67e, attr, _67b);
                            break;
                        case "innerHTML":
                            _67e.innerHTML = _67b;
                            break;
                        case "class":
                            dojo.removeClass(_67e, this[attr]);
                            dojo.addClass(_67e, _67b);
                            break;
                    }
                }, this);
                this[attr] = _67b;
            }, attr: function (name, _681) {
                var args = arguments.length;
                if (args == 1 && !dojo.isString(name)) {
                    for (var x in name) {
                        this.attr(x, name[x]);
                    }
                    return this;
                }
                var _684 = this._getAttrNames(name);
                if (args == 2) {
                    if (this[_684.s]) {
                        return this[_684.s](_681) || this;
                    } else {
                        if (name in this.attributeMap) {
                            this._attrToDom(name, _681);
                        }
                        this[name] = _681;
                    }
                    return this;
                } else {
                    if (this[_684.g]) {
                        return this[_684.g]();
                    } else {
                        return this[name];
                    }
                }
            }, _attrPairNames: {}, _getAttrNames: function (name) {
                var apn = this._attrPairNames;
                if (apn[name]) {
                    return apn[name];
                }
                var uc = name.charAt(0).toUpperCase() + name.substr(1);
                return apn[name] = {n: name + "Node", s: "_set" + uc + "Attr", g: "_get" + uc + "Attr"};
            }, toString: function () {
                return "[Widget " + this.declaredClass + ", " + (this.id || "NO ID") + "]";
            }, getDescendants: function () {
                if (this.containerNode) {
                    var list = dojo.query("[widgetId]", this.containerNode);
                    return list.map(dijit.byNode);
                } else {
                    return [];
                }
            }, nodesWithKeyClick: ["input", "button"], connect: function (obj, _68a, _68b) {
                var d = dojo;
                var dco = d.hitch(d, "connect", obj);
                var _68e = [];
                if (_68a == "ondijitclick") {
                    if (!this.nodesWithKeyClick[obj.nodeName]) {
                        var m = d.hitch(this, _68b);
                        _68e.push(dco("onkeydown", this, function (e) {
                            if (!d.isFF && e.keyCode == d.keys.ENTER) {
                                return m(e);
                            } else {
                                if (e.keyCode == d.keys.SPACE) {
                                    d.stopEvent(e);
                                }
                            }
                        }), dco("onkeyup", this, function (e) {
                            if (e.keyCode == d.keys.SPACE) {
                                return m(e);
                            }
                        }));
                        if (d.isFF) {
                            _68e.push(dco("onkeypress", this, function (e) {
                                if (e.keyCode == d.keys.ENTER) {
                                    return m(e);
                                }
                            }));
                        }
                    }
                    _68a = "onclick";
                }
                _68e.push(dco(_68a, this, _68b));
                this._connects.push(_68e);
                return _68e;
            }, disconnect: function (_693) {
                for (var i = 0; i < this._connects.length; i++) {
                    if (this._connects[i] == _693) {
                        dojo.forEach(_693, dojo.disconnect);
                        this._connects.splice(i, 1);
                        return;
                    }
                }
            }, isLeftToRight: function () {
                return dojo._isBodyLtr();
            }, isFocusable: function () {
                return this.focus && (dojo.style(this.domNode, "display") != "none");
            }, placeAt: function (_695, _696) {
                if (_695["declaredClass"] && _695["addChild"]) {
                    _695.addChild(this, _696);
                } else {
                    dojo.place(this.domNode, _695, _696);
                }
                return this;
            }});
        })();
    }
    if (!dojo._hasResource["dojo.string"]) {
        dojo._hasResource["dojo.string"] = true;
        dojo.provide("dojo.string");
        dojo.string.rep = function (str, num) {
            if (num <= 0 || !str) {
                return "";
            }
            var buf = [];
            for (; ;) {
                if (num & 1) {
                    buf.push(str);
                }
                if (!(num >>= 1)) {
                    break;
                }
                str += str;
            }
            return buf.join("");
        };
        dojo.string.pad = function (text, size, ch, end) {
            if (!ch) {
                ch = "0";
            }
            var out = String(text), pad = dojo.string.rep(ch, Math.ceil((size - out.length) / ch.length));
            return end ? out + pad : pad + out;
        };
        dojo.string.substitute = function (_6a0, map, _6a2, _6a3) {
            _6a3 = _6a3 || dojo.global;
            _6a2 = (!_6a2) ? function (v) {
                return v;
            } : dojo.hitch(_6a3, _6a2);
            return _6a0.replace(/\$\{([^\s\:\}]+)(?:\:([^\s\:\}]+))?\}/g, function (_6a5, key, _6a7) {
                var _6a8 = dojo.getObject(key, false, map);
                if (_6a7) {
                    _6a8 = dojo.getObject(_6a7, false, _6a3).call(_6a3, _6a8, key);
                }
                return _6a2(_6a8, key).toString();
            });
        };
        dojo.string.trim = function (str) {
            str = str.replace(/^\s+/, "");
            for (var i = str.length - 1; i >= 0; i--) {
                if (/\S/.test(str.charAt(i))) {
                    str = str.substring(0, i + 1);
                    break;
                }
            }
            return str;
        };
    }
    if (!dojo._hasResource["dojo.date.stamp"]) {
        dojo._hasResource["dojo.date.stamp"] = true;
        dojo.provide("dojo.date.stamp");
        dojo.date.stamp.fromISOString = function (_6ab, _6ac) {
            if (!dojo.date.stamp._isoRegExp) {
                dojo.date.stamp._isoRegExp = /^(?:(\d{4})(?:-(\d{2})(?:-(\d{2}))?)?)?(?:T(\d{2}):(\d{2})(?::(\d{2})(.\d+)?)?((?:[+-](\d{2}):(\d{2}))|Z)?)?$/;
            }
            var _6ad = dojo.date.stamp._isoRegExp.exec(_6ab);
            var _6ae = null;
            if (_6ad) {
                _6ad.shift();
                if (_6ad[1]) {
                    _6ad[1]--;
                }
                if (_6ad[6]) {
                    _6ad[6] *= 1000;
                }
                if (_6ac) {
                    _6ac = new Date(_6ac);
                    dojo.map(["FullYear", "Month", "Date", "Hours", "Minutes", "Seconds", "Milliseconds"],function (prop) {
                        return _6ac["get" + prop]();
                    }).forEach(function (_6b0, _6b1) {
                        if (_6ad[_6b1] === undefined) {
                            _6ad[_6b1] = _6b0;
                        }
                    });
                }
                _6ae = new Date(_6ad[0] || 1970, _6ad[1] || 0, _6ad[2] || 1, _6ad[3] || 0, _6ad[4] || 0, _6ad[5] || 0, _6ad[6] || 0);
                var _6b2 = 0;
                var _6b3 = _6ad[7] && _6ad[7].charAt(0);
                if (_6b3 != "Z") {
                    _6b2 = ((_6ad[8] || 0) * 60) + (Number(_6ad[9]) || 0);
                    if (_6b3 != "-") {
                        _6b2 *= -1;
                    }
                }
                if (_6b3) {
                    _6b2 -= _6ae.getTimezoneOffset();
                }
                if (_6b2) {
                    _6ae.setTime(_6ae.getTime() + _6b2 * 60000);
                }
            }
            return _6ae;
        };
        dojo.date.stamp.toISOString = function (_6b4, _6b5) {
            var _ = function (n) {
                return (n < 10) ? "0" + n : n;
            };
            _6b5 = _6b5 || {};
            var _6b8 = [];
            var _6b9 = _6b5.zulu ? "getUTC" : "get";
            var date = "";
            if (_6b5.selector != "time") {
                var year = _6b4[_6b9 + "FullYear"]();
                date = ["0000".substr((year + "").length) + year, _(_6b4[_6b9 + "Month"]() + 1), _(_6b4[_6b9 + "Date"]())].join("-");
            }
            _6b8.push(date);
            if (_6b5.selector != "date") {
                var time = [_(_6b4[_6b9 + "Hours"]()), _(_6b4[_6b9 + "Minutes"]()), _(_6b4[_6b9 + "Seconds"]())].join(":");
                var _6bd = _6b4[_6b9 + "Milliseconds"]();
                if (_6b5.milliseconds) {
                    time += "." + (_6bd < 100 ? "0" : "") + _(_6bd);
                }
                if (_6b5.zulu) {
                    time += "Z";
                } else {
                    if (_6b5.selector != "time") {
                        var _6be = _6b4.getTimezoneOffset();
                        var _6bf = Math.abs(_6be);
                        time += (_6be > 0 ? "-" : "+") + _(Math.floor(_6bf / 60)) + ":" + _(_6bf % 60);
                    }
                }
                _6b8.push(time);
            }
            return _6b8.join("T");
        };
    }
    if (!dojo._hasResource["dojo.parser"]) {
        dojo._hasResource["dojo.parser"] = true;
        dojo.provide("dojo.parser");
        dojo.parser = new function () {
            var d = dojo;
            var _6c1 = d._scopeName + "Type";
            var qry = "[" + _6c1 + "]";

            function val2type(_6c3) {
                if (d.isString(_6c3)) {
                    return "string";
                }
                if (typeof _6c3 == "number") {
                    return "number";
                }
                if (typeof _6c3 == "boolean") {
                    return "boolean";
                }
                if (d.isFunction(_6c3)) {
                    return "function";
                }
                if (d.isArray(_6c3)) {
                    return "array";
                }
                if (_6c3 instanceof Date) {
                    return "date";
                }
                if (_6c3 instanceof d._Url) {
                    return "url";
                }
                return "object";
            };
            function str2obj(_6c4, type) {
                switch (type) {
                    case "string":
                        return _6c4;
                    case "number":
                        return _6c4.length ? Number(_6c4) : NaN;
                    case "boolean":
                        return typeof _6c4 == "boolean" ? _6c4 : !(_6c4.toLowerCase() == "false");
                    case "function":
                        if (d.isFunction(_6c4)) {
                            _6c4 = _6c4.toString();
                            _6c4 = d.trim(_6c4.substring(_6c4.indexOf("{") + 1, _6c4.length - 1));
                        }
                        try {
                            if (_6c4.search(/[^\w\.]+/i) != -1) {
                                _6c4 = d.parser._nameAnonFunc(new Function(_6c4), this);
                            }
                            return d.getObject(_6c4, false);
                        } catch (e) {
                            return new Function();
                        }
                    case "array":
                        return _6c4 ? _6c4.split(/\s*,\s*/) : [];
                    case "date":
                        switch (_6c4) {
                            case "":
                                return new Date("");
                            case "now":
                                return new Date();
                            default:
                                return d.date.stamp.fromISOString(_6c4);
                        }
                    case "url":
                        return d.baseUrl + _6c4;
                    default:
                        return d.fromJson(_6c4);
                }
            };
            var _6c6 = {};

            function getClassInfo(_6c7) {
                if (!_6c6[_6c7]) {
                    var cls = d.getObject(_6c7);
                    if (!d.isFunction(cls)) {
                        throw new Error("Could not load class '" + _6c7 + "'. Did you spell the name correctly and use a full path, like 'dijit.form.Button'?");
                    }
                    var _6c9 = cls.prototype;
                    var _6ca = {};
                    for (var name in _6c9) {
                        if (name.charAt(0) == "_") {
                            continue;
                        }
                        var _6cc = _6c9[name];
                        _6ca[name] = val2type(_6cc);
                    }
                    _6c6[_6c7] = {cls: cls, params: _6ca};
                }
                return _6c6[_6c7];
            };
            this._functionFromScript = function (_6cd) {
                var _6ce = "";
                var _6cf = "";
                var _6d0 = _6cd.getAttribute("args");
                if (_6d0) {
                    d.forEach(_6d0.split(/\s*,\s*/), function (part, idx) {
                        _6ce += "var " + part + " = arguments[" + idx + "]; ";
                    });
                }
                var _6d3 = _6cd.getAttribute("with");
                if (_6d3 && _6d3.length) {
                    d.forEach(_6d3.split(/\s*,\s*/), function (part) {
                        _6ce += "with(" + part + "){";
                        _6cf += "}";
                    });
                }
                return new Function(_6ce + _6cd.innerHTML + _6cf);
            };
            this.instantiate = function (_6d5) {
                var _6d6 = [];
                d.forEach(_6d5, function (node) {
                    if (!node) {
                        return;
                    }
                    var type = node.getAttribute(_6c1);
                    if ((!type) || (!type.length)) {
                        return;
                    }
                    var _6d9 = getClassInfo(type);
                    var _6da = _6d9.cls;
                    var ps = _6da._noScript || _6da.prototype._noScript;
                    var _6dc = {};
                    var _6dd = node.attributes;
                    for (var name in _6d9.params) {
                        var item = _6dd.getNamedItem(name);
                        if (!item || (!item.specified && (!dojo.isIE || name.toLowerCase() != "value"))) {
                            continue;
                        }
                        var _6e0 = item.value;
                        switch (name) {
                            case "class":
                                _6e0 = node.className;
                                break;
                            case "style":
                                _6e0 = node.style && node.style.cssText;
                        }
                        var _6e1 = _6d9.params[name];
                        _6dc[name] = str2obj(_6e0, _6e1);
                    }
                    if (!ps) {
                        var _6e2 = [], _6e3 = [];
                        d.query("> script[type^='dojo/']", node).orphan().forEach(function (_6e4) {
                            var _6e5 = _6e4.getAttribute("event"), type = _6e4.getAttribute("type"), nf = d.parser._functionFromScript(_6e4);
                            if (_6e5) {
                                if (type == "dojo/connect") {
                                    _6e2.push({event: _6e5, func: nf});
                                } else {
                                    _6dc[_6e5] = nf;
                                }
                            } else {
                                _6e3.push(nf);
                            }
                        });
                    }
                    var _6e7 = _6da["markupFactory"];
                    if (!_6e7 && _6da["prototype"]) {
                        _6e7 = _6da.prototype["markupFactory"];
                    }
                    var _6e8 = _6e7 ? _6e7(_6dc, node, _6da) : new _6da(_6dc, node);
                    _6d6.push(_6e8);
                    var _6e9 = node.getAttribute("jsId");
                    if (_6e9) {
                        d.setObject(_6e9, _6e8);
                    }
                    if (!ps) {
                        d.forEach(_6e2, function (_6ea) {
                            d.connect(_6e8, _6ea.event, null, _6ea.func);
                        });
                        d.forEach(_6e3, function (func) {
                            func.call(_6e8);
                        });
                    }
                });
                d.forEach(_6d6, function (_6ec) {
                    if (_6ec && _6ec.startup && !_6ec._started && (!_6ec.getParent || !_6ec.getParent())) {
                        _6ec.startup();
                    }
                });
                return _6d6;
            };
            this.parse = function (_6ed) {
                var list = d.query(qry, _6ed);
                var _6ef = this.instantiate(list);
                return _6ef;
            };
        }();
        (function () {
            var _6f0 = function () {
                if (dojo.config["parseOnLoad"] == true) {
                    dojo.parser.parse();
                }
            };
            if (dojo.exists("dijit.wai.onload") && (dijit.wai.onload === dojo._loaders[0])) {
                dojo._loaders.splice(1, 0, _6f0);
            } else {
                dojo._loaders.unshift(_6f0);
            }
        })();
        dojo.parser._anonCtr = 0;
        dojo.parser._anon = {};
        dojo.parser._nameAnonFunc = function (_6f1, _6f2) {
            var jpn = "$joinpoint";
            var nso = (_6f2 || dojo.parser._anon);
            if (dojo.isIE) {
                var cn = _6f1["__dojoNameCache"];
                if (cn && nso[cn] === _6f1) {
                    return _6f1["__dojoNameCache"];
                }
            }
            var ret = "__" + dojo.parser._anonCtr++;
            while (typeof nso[ret] != "undefined") {
                ret = "__" + dojo.parser._anonCtr++;
            }
            nso[ret] = _6f1;
            return ret;
        };
    }
    if (!dojo._hasResource["dijit._Templated"]) {
        dojo._hasResource["dijit._Templated"] = true;
        dojo.provide("dijit._Templated");
        dojo.declare("dijit._Templated", null, {templateNode: null, templateString: null, templatePath: null, widgetsInTemplate: false, _skipNodeCache: false, _stringRepl: function (tmpl) {
            var _6f8 = this.declaredClass, _6f9 = this;
            return dojo.string.substitute(tmpl, this, function (_6fa, key) {
                if (key.charAt(0) == "!") {
                    _6fa = _6f9[key.substr(1)];
                }
                if (typeof _6fa == "undefined") {
                    throw new Error(_6f8 + " template:" + key);
                }
                if (_6fa == null) {
                    return "";
                }
                return key.charAt(0) == "!" ? _6fa : _6fa.toString().replace(/"/g, "&quot;");
            }, this);
        }, buildRendering: function () {
            var _6fc = dijit._Templated.getCachedTemplate(this.templatePath, this.templateString, this._skipNodeCache);
            var node;
            if (dojo.isString(_6fc)) {
                node = dijit._Templated._createNodesFromText(this._stringRepl(_6fc))[0];
            } else {
                node = _6fc.cloneNode(true);
            }
            this.domNode = node;
            this._attachTemplateNodes(node);
            var _6fe = this.srcNodeRef;
            if (_6fe && _6fe.parentNode) {
                _6fe.parentNode.replaceChild(node, _6fe);
            }
            if (this.widgetsInTemplate) {
                var cw = (this._supportingWidgets = dojo.parser.parse(node));
                this._attachTemplateNodes(cw, function (n, p) {
                    return n[p];
                });
            }
            this._fillContent(_6fe);
        }, _fillContent: function (_702) {
            var dest = this.containerNode;
            if (_702 && dest) {
                while (_702.hasChildNodes()) {
                    dest.appendChild(_702.firstChild);
                }
            }
        }, _attachTemplateNodes: function (_704, _705) {
            _705 = _705 || function (n, p) {
                return n.getAttribute(p);
            };
            var _708 = dojo.isArray(_704) ? _704 : (_704.all || _704.getElementsByTagName("*"));
            var x = dojo.isArray(_704) ? 0 : -1;
            var _70a = {};
            for (; x < _708.length; x++) {
                var _70b = (x == -1) ? _704 : _708[x];
                if (this.widgetsInTemplate && _705(_70b, "dojoType")) {
                    continue;
                }
                var _70c = _705(_70b, "dojoAttachPoint");
                if (_70c) {
                    var _70d, _70e = _70c.split(/\s*,\s*/);
                    while ((_70d = _70e.shift())) {
                        if (dojo.isArray(this[_70d])) {
                            this[_70d].push(_70b);
                        } else {
                            this[_70d] = _70b;
                        }
                    }
                }
                var _70f = _705(_70b, "dojoAttachEvent");
                if (_70f) {
                    var _710, _711 = _70f.split(/\s*,\s*/);
                    var trim = dojo.trim;
                    while ((_710 = _711.shift())) {
                        if (_710) {
                            var _713 = null;
                            if (_710.indexOf(":") != -1) {
                                var _714 = _710.split(":");
                                _710 = trim(_714[0]);
                                _713 = trim(_714[1]);
                            } else {
                                _710 = trim(_710);
                            }
                            if (!_713) {
                                _713 = _710;
                            }
                            this.connect(_70b, _710, _713);
                        }
                    }
                }
                var role = _705(_70b, "waiRole");
                if (role) {
                    dijit.setWaiRole(_70b, role);
                }
                var _716 = _705(_70b, "waiState");
                if (_716) {
                    dojo.forEach(_716.split(/\s*,\s*/), function (_717) {
                        if (_717.indexOf("-") != -1) {
                            var pair = _717.split("-");
                            dijit.setWaiState(_70b, pair[0], pair[1]);
                        }
                    });
                }
            }
        }});
        dijit._Templated._templateCache = {};
        dijit._Templated.getCachedTemplate = function (_719, _71a, _71b) {
            var _71c = dijit._Templated._templateCache;
            var key = _71a || _719;
            var _71e = _71c[key];
            if (_71e) {
                if (!_71e.ownerDocument || _71e.ownerDocument == dojo.doc) {
                    return _71e;
                }
                dojo._destroyElement(_71e);
            }
            if (!_71a) {
                _71a = dijit._Templated._sanitizeTemplateString(dojo._getText(_719));
            }
            _71a = dojo.string.trim(_71a);
            if (_71b || _71a.match(/\$\{([^\}]+)\}/g)) {
                return (_71c[key] = _71a);
            } else {
                return (_71c[key] = dijit._Templated._createNodesFromText(_71a)[0]);
            }
        };
        dijit._Templated._sanitizeTemplateString = function (_71f) {
            if (_71f) {
                _71f = _71f.replace(/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im, "");
                var _720 = _71f.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
                if (_720) {
                    _71f = _720[1];
                }
            } else {
                _71f = "";
            }
            return _71f;
        };
        if (dojo.isIE) {
            dojo.addOnWindowUnload(function () {
                var _721 = dijit._Templated._templateCache;
                for (var key in _721) {
                    var _723 = _721[key];
                    if (!isNaN(_723.nodeType)) {
                        dojo._destroyElement(_723);
                    }
                    delete _721[key];
                }
            });
        }
        (function () {
            var _724 = {cell: {re: /^<t[dh][\s\r\n>]/i, pre: "<table><tbody><tr>", post: "</tr></tbody></table>"}, row: {re: /^<tr[\s\r\n>]/i, pre: "<table><tbody>", post: "</tbody></table>"}, section: {re: /^<(thead|tbody|tfoot)[\s\r\n>]/i, pre: "<table>", post: "</table>"}};
            var tn;
            dijit._Templated._createNodesFromText = function (text) {
                if (tn && tn.ownerDocument != dojo.doc) {
                    dojo._destroyElement(tn);
                    tn = undefined;
                }
                if (!tn) {
                    tn = dojo.doc.createElement("div");
                    tn.style.display = "none";
                    dojo.body().appendChild(tn);
                }
                var _727 = "none";
                var _728 = text.replace(/^\s+/, "");
                for (var type in _724) {
                    var map = _724[type];
                    if (map.re.test(_728)) {
                        _727 = type;
                        text = map.pre + text + map.post;
                        break;
                    }
                }
                tn.innerHTML = text;
                if (tn.normalize) {
                    tn.normalize();
                }
                var tag = {cell: "tr", row: "tbody", section: "table"}[_727];
                var _72c = (typeof tag != "undefined") ? tn.getElementsByTagName(tag)[0] : tn;
                var _72d = [];
                while (_72c.firstChild) {
                    _72d.push(_72c.removeChild(_72c.firstChild));
                }
                tn.innerHTML = "";
                return _72d;
            };
        })();
        dojo.extend(dijit._Widget, {dojoAttachEvent: "", dojoAttachPoint: "", waiRole: "", waiState: ""});
    }
    if (!dojo._hasResource["dijit.form._FormWidget"]) {
        dojo._hasResource["dijit.form._FormWidget"] = true;
        dojo.provide("dijit.form._FormWidget");
        dojo.declare("dijit.form._FormWidget", [dijit._Widget, dijit._Templated], {baseClass: "", name: "", alt: "", value: "", type: "text", tabIndex: "0", disabled: false, readOnly: false, intermediateChanges: false, attributeMap: dojo.mixin(dojo.clone(dijit._Widget.prototype.attributeMap), {value: "focusNode", disabled: "focusNode", readOnly: "focusNode", id: "focusNode", tabIndex: "focusNode", alt: "focusNode"}), _setDisabledAttr: function (_72e) {
            this.disabled = _72e;
            dojo.attr(this.focusNode, "disabled", _72e);
            dijit.setWaiState(this.focusNode, "disabled", _72e);
            if (_72e) {
                this._hovering = false;
                this._active = false;
                this.focusNode.removeAttribute("tabIndex");
            } else {
                this.focusNode.setAttribute("tabIndex", this.tabIndex);
            }
            this._setStateClass();
        }, setDisabled: function (_72f) {
            dojo.deprecated("setDisabled(" + _72f + ") is deprecated. Use attr('disabled'," + _72f + ") instead.", "", "2.0");
            this.attr("disabled", _72f);
        }, _scroll: true, _onFocus: function (e) {
            if (this._scroll) {
                dijit.scrollIntoView(this.domNode);
            }
            this.inherited(arguments);
        }, _onMouse: function (_731) {
            var _732 = _731.currentTarget;
            if (_732 && _732.getAttribute) {
                this.stateModifier = _732.getAttribute("stateModifier") || "";
            }
            if (!this.disabled) {
                switch (_731.type) {
                    case "mouseenter":
                    case "mouseover":
                        this._hovering = true;
                        this._active = this._mouseDown;
                        break;
                    case "mouseout":
                    case "mouseleave":
                        this._hovering = false;
                        this._active = false;
                        break;
                    case "mousedown":
                        this._active = true;
                        this._mouseDown = true;
                        var _733 = this.connect(dojo.body(), "onmouseup", function () {
                            if (this._mouseDown && this.isFocusable()) {
                                this.focus();
                            }
                            this._active = false;
                            this._mouseDown = false;
                            this._setStateClass();
                            this.disconnect(_733);
                        });
                        break;
                }
                this._setStateClass();
            }
        }, isFocusable: function () {
            return !this.disabled && !this.readOnly && this.focusNode && (dojo.style(this.domNode, "display") != "none");
        }, focus: function () {
            dijit.focus(this.focusNode);
        }, _setStateClass: function () {
            var _734 = this.baseClass.split(" ");

            function multiply(_735) {
                _734 = _734.concat(dojo.map(_734, function (c) {
                    return c + _735;
                }), "dijit" + _735);
            };
            if (this.checked) {
                multiply("Checked");
            }
            if (this.state) {
                multiply(this.state);
            }
            if (this.selected) {
                multiply("Selected");
            }
            if (this.disabled) {
                multiply("Disabled");
            } else {
                if (this.readOnly) {
                    multiply("ReadOnly");
                } else {
                    if (this._active) {
                        multiply(this.stateModifier + "Active");
                    } else {
                        if (this._focused) {
                            multiply("Focused");
                        }
                        if (this._hovering) {
                            multiply(this.stateModifier + "Hover");
                        }
                    }
                }
            }
            var tn = this.stateNode || this.domNode, _738 = {};
            dojo.forEach(tn.className.split(" "), function (c) {
                _738[c] = true;
            });
            if ("_stateClasses" in this) {
                dojo.forEach(this._stateClasses, function (c) {
                    delete _738[c];
                });
            }
            dojo.forEach(_734, function (c) {
                _738[c] = true;
            });
            var _73c = [];
            for (var c in _738) {
                _73c.push(c);
            }
            tn.className = _73c.join(" ");
            this._stateClasses = _734;
        }, compare: function (val1, val2) {
            if ((typeof val1 == "number") && (typeof val2 == "number")) {
                return (isNaN(val1) && isNaN(val2)) ? 0 : (val1 - val2);
            } else {
                if (val1 > val2) {
                    return 1;
                } else {
                    if (val1 < val2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }, onChange: function (_740) {
        }, _onChangeActive: false, _handleOnChange: function (_741, _742) {
            this._lastValue = _741;
            if (this._lastValueReported == undefined && (_742 === null || !this._onChangeActive)) {
                this._resetValue = this._lastValueReported = _741;
            }
            if ((this.intermediateChanges || _742 || _742 === undefined) && ((typeof _741 != typeof this._lastValueReported) || this.compare(_741, this._lastValueReported) != 0)) {
                this._lastValueReported = _741;
                if (this._onChangeActive) {
                    this.onChange(_741);
                }
            }
        }, create: function () {
            this.inherited(arguments);
            this._onChangeActive = true;
            this._setStateClass();
        }, destroy: function () {
            if (this._layoutHackHandle) {
                clearTimeout(this._layoutHackHandle);
            }
            this.inherited(arguments);
        }, setValue: function (_743) {
            dojo.deprecated("dijit.form._FormWidget:setValue(" + _743 + ") is deprecated.  Use attr('value'," + _743 + ") instead.", "", "2.0");
            this.attr("value", _743);
        }, getValue: function () {
            dojo.deprecated(this.declaredClass + "::getValue() is deprecated. Use attr('value') instead.", "", "2.0");
            return this.attr("value");
        }, _layoutHack: function () {
            if (dojo.isFF == 2 && !this._layoutHackHandle) {
                var node = this.domNode;
                var old = node.style.opacity;
                node.style.opacity = "0.999";
                this._layoutHackHandle = setTimeout(dojo.hitch(this, function () {
                    this._layoutHackHandle = null;
                    node.style.opacity = old;
                }), 0);
            }
        }});
        dojo.declare("dijit.form._FormValueWidget", dijit.form._FormWidget, {attributeMap: dojo.mixin(dojo.clone(dijit.form._FormWidget.prototype.attributeMap), {value: ""}), postCreate: function () {
            if (dojo.isIE || dojo.isSafari) {
                this.connect(this.focusNode || this.domNode, "onkeydown", this._onKeyDown);
            }
            if (this._resetValue === undefined) {
                this._resetValue = this.value;
            }
        }, _setValueAttr: function (_746, _747) {
            this.value = _746;
            this._handleOnChange(_746, _747);
        }, _getValueAttr: function (_748) {
            return this._lastValue;
        }, undo: function () {
            this._setValueAttr(this._lastValueReported, false);
        }, reset: function () {
            this._hasBeenBlurred = false;
            this._setValueAttr(this._resetValue, true);
        }, _valueChanged: function () {
            var v = this.attr("value");
            var lv = this._lastValueReported;
            return ((v !== null && (v !== undefined) && v.toString) ? v.toString() : "") !== ((lv !== null && (lv !== undefined) && lv.toString) ? lv.toString() : "");
        }, _onKeyDown: function (e) {
            if (e.keyCode == dojo.keys.ESCAPE && !e.ctrlKey && !e.altKey) {
                var te;
                if (dojo.isIE) {
                    e.preventDefault();
                    te = document.createEventObject();
                    te.keyCode = dojo.keys.ESCAPE;
                    te.shiftKey = e.shiftKey;
                    e.srcElement.fireEvent("onkeypress", te);
                } else {
                    if (dojo.isSafari) {
                        te = document.createEvent("Events");
                        te.initEvent("keypress", true, true);
                        te.keyCode = dojo.keys.ESCAPE;
                        te.shiftKey = e.shiftKey;
                        e.target.dispatchEvent(te);
                    }
                }
            }
        }, _onKeyPress: function (e) {
            if (e.charOrCode == dojo.keys.ESCAPE && !e.ctrlKey && !e.altKey && this._valueChanged()) {
                this.undo();
                dojo.stopEvent(e);
                return false;
            } else {
                if (this.intermediateChanges) {
                    var _74e = this;
                    setTimeout(function () {
                        _74e._handleOnChange(_74e.attr("value"), false);
                    }, 0);
                }
            }
            return true;
        }});
    }
    if (!dojo._hasResource["dijit.form.TextBox"]) {
        dojo._hasResource["dijit.form.TextBox"] = true;
        dojo.provide("dijit.form.TextBox");
        dojo.declare("dijit.form.TextBox", dijit.form._FormValueWidget, {trim: false, uppercase: false, lowercase: false, propercase: false, maxLength: "", templateString: "<input class=\"dijit dijitReset dijitLeft\" dojoAttachPoint='textbox,focusNode' name=\"${name}\"\n\tdojoAttachEvent='onmouseenter:_onMouse,onmouseleave:_onMouse,onfocus:_onMouse,onblur:_onMouse,onkeypress:_onKeyPress'\n\tautocomplete=\"off\" type=\"${type}\"\n\t/>\n", baseClass: "dijitTextBox", attributeMap: dojo.mixin(dojo.clone(dijit.form._FormValueWidget.prototype.attributeMap), {maxLength: "focusNode"}), _getValueAttr: function () {
            return this.parse(this.attr("displayedValue"), this.constraints);
        }, _setValueAttr: function (_74f, _750, _751) {
            var _752;
            if (_74f !== undefined) {
                _752 = this.filter(_74f);
                if (_752 !== null && ((typeof _752 != "number") || !isNaN(_752))) {
                    if (typeof _751 != "string") {
                        _751 = this.format(_752, this.constraints);
                    }
                } else {
                    _751 = "";
                }
            }
            if (_751 != null && _751 != undefined) {
                this.textbox.value = _751;
            }
            dijit.form.TextBox.superclass._setValueAttr.call(this, _752, _750);
        }, displayedValue: "", getDisplayedValue: function () {
            dojo.deprecated(this.declaredClass + "::getDisplayedValue() is deprecated. Use attr('displayedValue') instead.", "", "2.0");
            return this.attr("displayedValue");
        }, _getDisplayedValueAttr: function () {
            return this.filter(this.textbox.value);
        }, setDisplayedValue: function (_753) {
            dojo.deprecated(this.declaredClass + "::setDisplayedValue() is deprecated. Use attr('displayedValue', ...) instead.", "", "2.0");
            this.attr("displayedValue", _753);
        }, _setDisplayedValueAttr: function (_754) {
            this.textbox.value = _754;
            this._setValueAttr(this.attr("value"));
        }, format: function (_755, _756) {
            return ((_755 == null || _755 == undefined) ? "" : (_755.toString ? _755.toString() : _755));
        }, parse: function (_757, _758) {
            return _757;
        }, postCreate: function () {
            this.textbox.setAttribute("value", this.textbox.value);
            this.inherited(arguments);
            this._layoutHack();
        }, filter: function (val) {
            if (typeof val != "string") {
                return val;
            }
            if (this.trim) {
                val = dojo.trim(val);
            }
            if (this.uppercase) {
                val = val.toUpperCase();
            }
            if (this.lowercase) {
                val = val.toLowerCase();
            }
            if (this.propercase) {
                val = val.replace(/[^\s]+/g, function (word) {
                    return word.substring(0, 1).toUpperCase() + word.substring(1);
                });
            }
            return val;
        }, _setBlurValue: function () {
            this._setValueAttr(this.attr("value"), (this.isValid ? this.isValid() : true));
        }, _onBlur: function () {
            this._setBlurValue();
            this.inherited(arguments);
        }});
        dijit.selectInputText = function (_75b, _75c, stop) {
            var _75e = dojo.global;
            var _75f = dojo.doc;
            _75b = dojo.byId(_75b);
            if (isNaN(_75c)) {
                _75c = 0;
            }
            if (isNaN(stop)) {
                stop = _75b.value ? _75b.value.length : 0;
            }
            _75b.focus();
            if (_75f["selection"] && dojo.body()["createTextRange"]) {
                if (_75b.createTextRange) {
                    var _760 = _75b.createTextRange();
                    with (_760) {
                        collapse(true);
                        moveStart("character", _75c);
                        moveEnd("character", stop);
                        select();
                    }
                }
            } else {
                if (_75e["getSelection"]) {
                    var _761 = _75e.getSelection();
                    if (_75b.setSelectionRange) {
                        _75b.setSelectionRange(_75c, stop);
                    }
                }
            }
        };
    }
    if (!dojo._hasResource["dijit.Tooltip"]) {
        dojo._hasResource["dijit.Tooltip"] = true;
        dojo.provide("dijit.Tooltip");
        dojo.declare("dijit._MasterTooltip", [dijit._Widget, dijit._Templated], {duration: dijit.defaultDuration, templateString: "<div class=\"dijitTooltip dijitTooltipLeft\" id=\"dojoTooltip\">\n\t<div class=\"dijitTooltipContainer dijitTooltipContents\" dojoAttachPoint=\"containerNode\" waiRole='alert'></div>\n\t<div class=\"dijitTooltipConnector\"></div>\n</div>\n", postCreate: function () {
            dojo.body().appendChild(this.domNode);
            this.bgIframe = new dijit.BackgroundIframe(this.domNode);
            this.fadeIn = dojo.fadeIn({node: this.domNode, duration: this.duration, onEnd: dojo.hitch(this, "_onShow")});
            this.fadeOut = dojo.fadeOut({node: this.domNode, duration: this.duration, onEnd: dojo.hitch(this, "_onHide")});
        }, show: function (_762, _763, _764) {
            if (this.aroundNode && this.aroundNode === _763) {
                return;
            }
            if (this.fadeOut.status() == "playing") {
                this._onDeck = arguments;
                return;
            }
            this.containerNode.innerHTML = _762;
            this.domNode.style.top = (this.domNode.offsetTop + 1) + "px";
            var _765 = {};
            var ltr = this.isLeftToRight();
            dojo.forEach((_764 && _764.length) ? _764 : dijit.Tooltip.defaultPosition, function (pos) {
                switch (pos) {
                    case "after":
                        _765[ltr ? "BR" : "BL"] = ltr ? "BL" : "BR";
                        break;
                    case "before":
                        _765[ltr ? "BL" : "BR"] = ltr ? "BR" : "BL";
                        break;
                    case "below":
                        _765[ltr ? "BL" : "BR"] = ltr ? "TL" : "TR";
                        _765[ltr ? "BR" : "BL"] = ltr ? "TR" : "TL";
                        break;
                    case "above":
                    default:
                        _765[ltr ? "TL" : "TR"] = ltr ? "BL" : "BR";
                        _765[ltr ? "TR" : "TL"] = ltr ? "BR" : "BL";
                        break;
                }
            });
            var pos = dijit.placeOnScreenAroundElement(this.domNode, _763, _765, dojo.hitch(this, "orient"));
            dojo.style(this.domNode, "opacity", 0);
            this.fadeIn.play();
            this.isShowingNow = true;
            this.aroundNode = _763;
        }, orient: function (node, _76a, _76b) {
            node.className = "dijitTooltip " + {"BL-TL": "dijitTooltipBelow dijitTooltipABLeft", "TL-BL": "dijitTooltipAbove dijitTooltipABLeft", "BR-TR": "dijitTooltipBelow dijitTooltipABRight", "TR-BR": "dijitTooltipAbove dijitTooltipABRight", "BR-BL": "dijitTooltipRight", "BL-BR": "dijitTooltipLeft"}[_76a + "-" + _76b];
        }, _onShow: function () {
            if (dojo.isIE) {
                this.domNode.style.filter = "";
            }
        }, hide: function (_76c) {
            if (this._onDeck && this._onDeck[1] == _76c) {
                this._onDeck = null;
            } else {
                if (this.aroundNode === _76c) {
                    this.fadeIn.stop();
                    this.isShowingNow = false;
                    this.aroundNode = null;
                    this.fadeOut.play();
                } else {
                }
            }
        }, _onHide: function () {
            this.domNode.style.cssText = "";
            if (this._onDeck) {
                this.show.apply(this, this._onDeck);
                this._onDeck = null;
            }
        }});
        dijit.showTooltip = function (_76d, _76e, _76f) {
            if (!dijit._masterTT) {
                dijit._masterTT = new dijit._MasterTooltip();
            }
            return dijit._masterTT.show(_76d, _76e, _76f);
        };
        dijit.hideTooltip = function (_770) {
            if (!dijit._masterTT) {
                dijit._masterTT = new dijit._MasterTooltip();
            }
            return dijit._masterTT.hide(_770);
        };
        dojo.declare("dijit.Tooltip", dijit._Widget, {label: "", showDelay: 400, connectId: [], position: [], postCreate: function () {
            dojo.addClass(this.domNode, "dijitTooltipData");
            this._connectNodes = [];
            dojo.forEach(this.connectId, function (id) {
                var node = dojo.byId(id);
                if (node) {
                    this._connectNodes.push(node);
                    dojo.forEach(["onMouseEnter", "onMouseLeave", "onFocus", "onBlur"], function (_773) {
                        this.connect(node, _773.toLowerCase(), "_" + _773);
                    }, this);
                    if (dojo.isIE) {
                        node.style.zoom = 1;
                    }
                }
            }, this);
        }, _onMouseEnter: function (e) {
            this._onHover(e);
        }, _onMouseLeave: function (e) {
            this._onUnHover(e);
        }, _onFocus: function (e) {
            this._focus = true;
            this._onHover(e);
            this.inherited(arguments);
        }, _onBlur: function (e) {
            this._focus = false;
            this._onUnHover(e);
            this.inherited(arguments);
        }, _onHover: function (e) {
            if (!this._showTimer) {
                var _779 = e.target;
                this._showTimer = setTimeout(dojo.hitch(this, function () {
                    this.open(_779);
                }), this.showDelay);
            }
        }, _onUnHover: function (e) {
            if (this._focus) {
                return;
            }
            if (this._showTimer) {
                clearTimeout(this._showTimer);
                delete this._showTimer;
            }
            this.close();
        }, open: function (_77b) {
            _77b = _77b || this._connectNodes[0];
            if (!_77b) {
                return;
            }
            if (this._showTimer) {
                clearTimeout(this._showTimer);
                delete this._showTimer;
            }
            dijit.showTooltip(this.label || this.domNode.innerHTML, _77b, this.position);
            this._connectNode = _77b;
        }, close: function () {
            if (this._connectNode) {
                dijit.hideTooltip(this._connectNode);
                delete this._connectNode;
            }
            if (this._showTimer) {
                clearTimeout(this._showTimer);
                delete this._showTimer;
            }
        }, uninitialize: function () {
            this.close();
        }});
        dijit.Tooltip.defaultPosition = ["after", "before"];
    }
    if (!dojo._hasResource["dijit.form.ValidationTextBox"]) {
        dojo._hasResource["dijit.form.ValidationTextBox"] = true;
        dojo.provide("dijit.form.ValidationTextBox");
        dojo.declare("dijit.form.ValidationTextBox", dijit.form.TextBox, {templateString: "<div class=\"dijit dijitReset dijitInlineTable dijitLeft\"\n\tid=\"widget_${id}\"\n\tdojoAttachEvent=\"onmouseenter:_onMouse,onmouseleave:_onMouse,onmousedown:_onMouse\" waiRole=\"presentation\"\n\t><div style=\"overflow:hidden;\"\n\t\t><div class=\"dijitReset dijitValidationIcon\"><br></div\n\t\t><div class=\"dijitReset dijitValidationIconText\">&Chi;</div\n\t\t><div class=\"dijitReset dijitInputField\"\n\t\t\t><input class=\"dijitReset\" dojoAttachPoint='textbox,focusNode' dojoAttachEvent='onfocus:_update,onkeyup:_update,onblur:_onMouse,onkeypress:_onKeyPress' autocomplete=\"off\"\n\t\t\ttype='${type}' name='${name}'\n\t\t/></div\n\t></div\n></div>\n", baseClass: "dijitTextBox", required: false, promptMessage: "", invalidMessage: "$_unset_$", constraints: {}, regExp: ".*", regExpGen: function (_77c) {
            return this.regExp;
        }, state: "", tooltipPosition: [], _setValueAttr: function () {
            this.inherited(arguments);
            this.validate(this._focused);
        }, validator: function (_77d, _77e) {
            return (new RegExp("^(?:" + this.regExpGen(_77e) + ")" + (this.required ? "" : "?") + "$")).test(_77d) && (!this.required || !this._isEmpty(_77d)) && (this._isEmpty(_77d) || this.parse(_77d, _77e) !== undefined);
        }, _isValidSubset: function () {
            return this.textbox.value.search(this._partialre) == 0;
        }, isValid: function (_77f) {
            return this.validator(this.textbox.value, this.constraints);
        }, _isEmpty: function (_780) {
            return /^\s*$/.test(_780);
        }, getErrorMessage: function (_781) {
            return this.invalidMessage;
        }, getPromptMessage: function (_782) {
            return this.promptMessage;
        }, _maskValidSubsetError: true, validate: function (_783) {
            var _784 = "";
            var _785 = this.disabled || this.isValid(_783);
            if (_785) {
                this._maskValidSubsetError = true;
            }
            var _786 = !_785 && _783 && this._isValidSubset();
            var _787 = this._isEmpty(this.textbox.value);
            this.state = (_785 || (!this._hasBeenBlurred && _787) || _786) ? "" : "Error";
            if (this.state == "Error") {
                this._maskValidSubsetError = false;
            }
            this._setStateClass();
            dijit.setWaiState(this.focusNode, "invalid", _785 ? "false" : "true");
            if (_783) {
                if (_787) {
                    _784 = this.getPromptMessage(true);
                }
                if (!_784 && (this.state == "Error" || (_786 && !this._maskValidSubsetError))) {
                    _784 = this.getErrorMessage(true);
                }
            }
            this.displayMessage(_784);
            return _785;
        }, _message: "", displayMessage: function (_788) {
            if (this._message == _788) {
                return;
            }
            this._message = _788;
            dijit.hideTooltip(this.domNode);
            if (_788) {
                dijit.showTooltip(_788, this.domNode, this.tooltipPosition);
            }
        }, _refreshState: function () {
            this.validate(this._focused);
        }, _update: function (e) {
            this._refreshState();
            this._onMouse(e);
        }, constructor: function () {
            this.constraints = {};
        }, postMixInProperties: function () {
            this.inherited(arguments);
            this.constraints.locale = this.lang;
            this.messages = dojo.i18n.getLocalization("dijit.form", "validate", this.lang);
            if (this.invalidMessage == "$_unset_$") {
                this.invalidMessage = this.messages.invalidMessage;
            }
            var p = this.regExpGen(this.constraints);
            this.regExp = p;
            var _78b = "";
            if (p != ".*") {
                this.regExp.replace(/\\.|\[\]|\[.*?[^\\]{1}\]|\{.*?\}|\(\?[=:!]|./g, function (re) {
                    switch (re.charAt(0)) {
                        case "{":
                        case "+":
                        case "?":
                        case "*":
                        case "^":
                        case "$":
                        case "|":
                        case "(":
                            _78b += re;
                            break;
                        case ")":
                            _78b += "|$)";
                            break;
                        default:
                            _78b += "(?:" + re + "|$)";
                            break;
                    }
                });
            }
            try {
                "".search(_78b);
            } catch (e) {
                _78b = this.regExp;
                console.debug("RegExp error in " + this.declaredClass + ": " + this.regExp);
            }
            this._partialre = "^(?:" + _78b + ")$";
        }, _setDisabledAttr: function (_78d) {
            this.inherited(arguments);
            if (this.valueNode) {
                this.valueNode.disabled = _78d;
            }
            this._refreshState();
        }, _setRequiredAttr: function (_78e) {
            this.required = _78e;
            dijit.setWaiState(this.focusNode, "required", _78e);
            this._refreshState();
        }, postCreate: function () {
            if (dojo.isIE) {
                var s = dojo.getComputedStyle(this.focusNode);
                if (s) {
                    var ff = s.fontFamily;
                    if (ff) {
                        this.focusNode.style.fontFamily = ff;
                    }
                }
            }
            this.inherited(arguments);
        }});
        dojo.declare("dijit.form.MappedTextBox", dijit.form.ValidationTextBox, {serialize: function (val, _792) {
            return val.toString ? val.toString() : "";
        }, toString: function () {
            var val = this.filter(this.attr("value"));
            return val != null ? (typeof val == "string" ? val : this.serialize(val, this.constraints)) : "";
        }, validate: function () {
            this.valueNode.value = this.toString();
            return this.inherited(arguments);
        }, buildRendering: function () {
            this.inherited(arguments);
            var _794 = this.textbox;
            var _795 = (this.valueNode = dojo.doc.createElement("input"));
            _795.setAttribute("type", _794.type);
            dojo.style(_795, "display", "none");
            this.valueNode.name = this.textbox.name;
            dojo.place(_795, _794, "after");
            this.textbox.name = this.textbox.name + "_displayed_";
            this.textbox.removeAttribute("name");
        }, _setDisabledAttr: function (_796) {
            this.inherited(arguments);
            dojo.attr(this.valueNode, "disabled", _796);
        }});
        dojo.declare("dijit.form.RangeBoundTextBox", dijit.form.MappedTextBox, {rangeMessage: "", rangeCheck: function (_797, _798) {
            var _799 = "min" in _798;
            var _79a = "max" in _798;
            if (_799 || _79a) {
                return (!_799 || this.compare(_797, _798.min) >= 0) && (!_79a || this.compare(_797, _798.max) <= 0);
            }
            return true;
        }, isInRange: function (_79b) {
            return this.rangeCheck(this.attr("value"), this.constraints);
        }, _isDefinitelyOutOfRange: function () {
            var val = this.attr("value");
            var _79d = false;
            var _79e = false;
            if ("min" in this.constraints) {
                var min = this.constraints.min;
                val = this.compare(val, ((typeof min == "number") && min >= 0 && val != 0) ? 0 : min);
                _79d = (typeof val == "number") && val < 0;
            }
            if ("max" in this.constraints) {
                var max = this.constraints.max;
                val = this.compare(val, ((typeof max != "number") || max > 0) ? max : 0);
                _79e = (typeof val == "number") && val > 0;
            }
            return _79d || _79e;
        }, _isValidSubset: function () {
            return this.inherited(arguments) && !this._isDefinitelyOutOfRange();
        }, isValid: function (_7a1) {
            return this.inherited(arguments) && ((this._isEmpty(this.textbox.value) && !this.required) || this.isInRange(_7a1));
        }, getErrorMessage: function (_7a2) {
            if (dijit.form.RangeBoundTextBox.superclass.isValid.call(this, false) && !this.isInRange(_7a2)) {
                return this.rangeMessage;
            }
            return this.inherited(arguments);
        }, postMixInProperties: function () {
            this.inherited(arguments);
            if (!this.rangeMessage) {
                this.messages = dojo.i18n.getLocalization("dijit.form", "validate", this.lang);
                this.rangeMessage = this.messages.rangeMessage;
            }
        }, postCreate: function () {
            this.inherited(arguments);
            if (this.constraints.min !== undefined) {
                dijit.setWaiState(this.focusNode, "valuemin", this.constraints.min);
            }
            if (this.constraints.max !== undefined) {
                dijit.setWaiState(this.focusNode, "valuemax", this.constraints.max);
            }
        }, _setValueAttr: function (_7a3, _7a4) {
            dijit.setWaiState(this.focusNode, "valuenow", _7a3);
            this.inherited(arguments);
        }});
    }
    if (!dojo._hasResource["dojo.regexp"]) {
        dojo._hasResource["dojo.regexp"] = true;
        dojo.provide("dojo.regexp");
        dojo.regexp.escapeString = function (str, _7a6) {
            return str.replace(/([\.$?*!=:|{}\(\)\[\]\\\/^])/g, function (ch) {
                if (_7a6 && _7a6.indexOf(ch) != -1) {
                    return ch;
                }
                return "\\" + ch;
            });
        };
        dojo.regexp.buildGroupRE = function (arr, re, _7aa) {
            if (!(arr instanceof Array)) {
                return re(arr);
            }
            var b = [];
            for (var i = 0; i < arr.length; i++) {
                b.push(re(arr[i]));
            }
            return dojo.regexp.group(b.join("|"), _7aa);
        };
        dojo.regexp.group = function (_7ad, _7ae) {
            return "(" + (_7ae ? "?:" : "") + _7ad + ")";
        };
    }
    if (!dojo._hasResource["dojo.number"]) {
        dojo._hasResource["dojo.number"] = true;
        dojo.provide("dojo.number");
        dojo.number.format = function (_7af, _7b0) {
            _7b0 = dojo.mixin({}, _7b0 || {});
            var _7b1 = dojo.i18n.normalizeLocale(_7b0.locale);
            var _7b2 = dojo.i18n.getLocalization("dojo.cldr", "number", _7b1);
            _7b0.customs = _7b2;
            var _7b3 = _7b0.pattern || _7b2[(_7b0.type || "decimal") + "Format"];
            if (isNaN(_7af)) {
                return null;
            }
            return dojo.number._applyPattern(_7af, _7b3, _7b0);
        };
        dojo.number._numberPatternRE = /[#0,]*[#0](?:\.0*#*)?/;
        dojo.number._applyPattern = function (_7b4, _7b5, _7b6) {
            _7b6 = _7b6 || {};
            var _7b7 = _7b6.customs.group;
            var _7b8 = _7b6.customs.decimal;
            var _7b9 = _7b5.split(";");
            var _7ba = _7b9[0];
            _7b5 = _7b9[(_7b4 < 0) ? 1 : 0] || ("-" + _7ba);
            if (_7b5.indexOf("%") != -1) {
                _7b4 *= 100;
            } else {
                if (_7b5.indexOf("‰") != -1) {
                    _7b4 *= 1000;
                } else {
                    if (_7b5.indexOf("¤") != -1) {
                        _7b7 = _7b6.customs.currencyGroup || _7b7;
                        _7b8 = _7b6.customs.currencyDecimal || _7b8;
                        _7b5 = _7b5.replace(/\u00a4{1,3}/, function (_7bb) {
                            var prop = ["symbol", "currency", "displayName"][_7bb.length - 1];
                            return _7b6[prop] || _7b6.currency || "";
                        });
                    } else {
                        if (_7b5.indexOf("E") != -1) {
                            throw new Error("exponential notation not supported");
                        }
                    }
                }
            }
            var _7bd = dojo.number._numberPatternRE;
            var _7be = _7ba.match(_7bd);
            if (!_7be) {
                throw new Error("unable to find a number expression in pattern: " + _7b5);
            }
            if (_7b6.fractional === false) {
                _7b6.places = 0;
            }
            return _7b5.replace(_7bd, dojo.number._formatAbsolute(_7b4, _7be[0], {decimal: _7b8, group: _7b7, places: _7b6.places, round: _7b6.round}));
        };
        dojo.number.round = function (_7bf, _7c0, _7c1) {
            var _7c2 = String(_7bf).split(".");
            var _7c3 = (_7c2[1] && _7c2[1].length) || 0;
            if (_7c3 > _7c0) {
                var _7c4 = Math.pow(10, _7c0);
                if (_7c1 > 0) {
                    _7c4 *= 10 / _7c1;
                    _7c0++;
                }
                _7bf = Math.round(_7bf * _7c4) / _7c4;
                _7c2 = String(_7bf).split(".");
                _7c3 = (_7c2[1] && _7c2[1].length) || 0;
                if (_7c3 > _7c0) {
                    _7c2[1] = _7c2[1].substr(0, _7c0);
                    _7bf = Number(_7c2.join("."));
                }
            }
            return _7bf;
        };
        dojo.number._formatAbsolute = function (_7c5, _7c6, _7c7) {
            _7c7 = _7c7 || {};
            if (_7c7.places === true) {
                _7c7.places = 0;
            }
            if (_7c7.places === Infinity) {
                _7c7.places = 6;
            }
            var _7c8 = _7c6.split(".");
            var _7c9 = (_7c7.places >= 0) ? _7c7.places : (_7c8[1] && _7c8[1].length) || 0;
            if (!(_7c7.round < 0)) {
                _7c5 = dojo.number.round(_7c5, _7c9, _7c7.round);
            }
            var _7ca = String(Math.abs(_7c5)).split(".");
            var _7cb = _7ca[1] || "";
            if (_7c7.places) {
                var _7cc = dojo.isString(_7c7.places) && _7c7.places.indexOf(",");
                if (_7cc) {
                    _7c7.places = _7c7.places.substring(_7cc + 1);
                }
                _7ca[1] = dojo.string.pad(_7cb.substr(0, _7c7.places), _7c7.places, "0", true);
            } else {
                if (_7c8[1] && _7c7.places !== 0) {
                    var pad = _7c8[1].lastIndexOf("0") + 1;
                    if (pad > _7cb.length) {
                        _7ca[1] = dojo.string.pad(_7cb, pad, "0", true);
                    }
                    var _7ce = _7c8[1].length;
                    if (_7ce < _7cb.length) {
                        _7ca[1] = _7cb.substr(0, _7ce);
                    }
                } else {
                    if (_7ca[1]) {
                        _7ca.pop();
                    }
                }
            }
            var _7cf = _7c8[0].replace(",", "");
            pad = _7cf.indexOf("0");
            if (pad != -1) {
                pad = _7cf.length - pad;
                if (pad > _7ca[0].length) {
                    _7ca[0] = dojo.string.pad(_7ca[0], pad);
                }
                if (_7cf.indexOf("#") == -1) {
                    _7ca[0] = _7ca[0].substr(_7ca[0].length - pad);
                }
            }
            var _7d0 = _7c8[0].lastIndexOf(",");
            var _7d1, _7d2;
            if (_7d0 != -1) {
                _7d1 = _7c8[0].length - _7d0 - 1;
                var _7d3 = _7c8[0].substr(0, _7d0);
                _7d0 = _7d3.lastIndexOf(",");
                if (_7d0 != -1) {
                    _7d2 = _7d3.length - _7d0 - 1;
                }
            }
            var _7d4 = [];
            for (var _7d5 = _7ca[0]; _7d5;) {
                var off = _7d5.length - _7d1;
                _7d4.push((off > 0) ? _7d5.substr(off) : _7d5);
                _7d5 = (off > 0) ? _7d5.slice(0, off) : "";
                if (_7d2) {
                    _7d1 = _7d2;
                    delete _7d2;
                }
            }
            _7ca[0] = _7d4.reverse().join(_7c7.group || ",");
            return _7ca.join(_7c7.decimal || ".");
        };
        dojo.number.regexp = function (_7d7) {
            return dojo.number._parseInfo(_7d7).regexp;
        };
        dojo.number._parseInfo = function (_7d8) {
            _7d8 = _7d8 || {};
            var _7d9 = dojo.i18n.normalizeLocale(_7d8.locale);
            var _7da = dojo.i18n.getLocalization("dojo.cldr", "number", _7d9);
            var _7db = _7d8.pattern || _7da[(_7d8.type || "decimal") + "Format"];
            var _7dc = _7da.group;
            var _7dd = _7da.decimal;
            var _7de = 1;
            if (_7db.indexOf("%") != -1) {
                _7de /= 100;
            } else {
                if (_7db.indexOf("‰") != -1) {
                    _7de /= 1000;
                } else {
                    var _7df = _7db.indexOf("¤") != -1;
                    if (_7df) {
                        _7dc = _7da.currencyGroup || _7dc;
                        _7dd = _7da.currencyDecimal || _7dd;
                    }
                }
            }
            var _7e0 = _7db.split(";");
            if (_7e0.length == 1) {
                _7e0.push("-" + _7e0[0]);
            }
            var re = dojo.regexp.buildGroupRE(_7e0, function (_7e2) {
                _7e2 = "(?:" + dojo.regexp.escapeString(_7e2, ".") + ")";
                return _7e2.replace(dojo.number._numberPatternRE, function (_7e3) {
                    var _7e4 = {signed: false, separator: _7d8.strict ? _7dc : [_7dc, ""], fractional: _7d8.fractional, decimal: _7dd, exponent: false};
                    var _7e5 = _7e3.split(".");
                    var _7e6 = _7d8.places;
                    if (_7e5.length == 1 || _7e6 === 0) {
                        _7e4.fractional = false;
                    } else {
                        if (_7e6 === undefined) {
                            _7e6 = _7d8.pattern ? _7e5[1].lastIndexOf("0") + 1 : Infinity;
                        }
                        if (_7e6 && _7d8.fractional == undefined) {
                            _7e4.fractional = true;
                        }
                        if (!_7d8.places && (_7e6 < _7e5[1].length)) {
                            _7e6 += "," + _7e5[1].length;
                        }
                        _7e4.places = _7e6;
                    }
                    var _7e7 = _7e5[0].split(",");
                    if (_7e7.length > 1) {
                        _7e4.groupSize = _7e7.pop().length;
                        if (_7e7.length > 1) {
                            _7e4.groupSize2 = _7e7.pop().length;
                        }
                    }
                    return "(" + dojo.number._realNumberRegexp(_7e4) + ")";
                });
            }, true);
            if (_7df) {
                re = re.replace(/([\s\xa0]*)(\u00a4{1,3})([\s\xa0]*)/g, function (_7e8, _7e9, _7ea, _7eb) {
                    var prop = ["symbol", "currency", "displayName"][_7ea.length - 1];
                    var _7ed = dojo.regexp.escapeString(_7d8[prop] || _7d8.currency || "");
                    _7e9 = _7e9 ? "[\\s\\xa0]" : "";
                    _7eb = _7eb ? "[\\s\\xa0]" : "";
                    if (!_7d8.strict) {
                        if (_7e9) {
                            _7e9 += "*";
                        }
                        if (_7eb) {
                            _7eb += "*";
                        }
                        return "(?:" + _7e9 + _7ed + _7eb + ")?";
                    }
                    return _7e9 + _7ed + _7eb;
                });
            }
            return {regexp: re.replace(/[\xa0 ]/g, "[\\s\\xa0]"), group: _7dc, decimal: _7dd, factor: _7de};
        };
        dojo.number.parse = function (_7ee, _7ef) {
            var info = dojo.number._parseInfo(_7ef);
            var _7f1 = (new RegExp("^" + info.regexp + "$")).exec(_7ee);
            if (!_7f1) {
                return NaN;
            }
            var _7f2 = _7f1[1];
            if (!_7f1[1]) {
                if (!_7f1[2]) {
                    return NaN;
                }
                _7f2 = _7f1[2];
                info.factor *= -1;
            }
            _7f2 = _7f2.replace(new RegExp("[" + info.group + "\\s\\xa0" + "]", "g"), "").replace(info.decimal, ".");
            return Number(_7f2) * info.factor;
        };
        dojo.number._realNumberRegexp = function (_7f3) {
            _7f3 = _7f3 || {};
            if (!("places" in _7f3)) {
                _7f3.places = Infinity;
            }
            if (typeof _7f3.decimal != "string") {
                _7f3.decimal = ".";
            }
            if (!("fractional" in _7f3) || /^0/.test(_7f3.places)) {
                _7f3.fractional = [true, false];
            }
            if (!("exponent" in _7f3)) {
                _7f3.exponent = [true, false];
            }
            if (!("eSigned" in _7f3)) {
                _7f3.eSigned = [true, false];
            }
            var _7f4 = dojo.number._integerRegexp(_7f3);
            var _7f5 = dojo.regexp.buildGroupRE(_7f3.fractional, function (q) {
                var re = "";
                if (q && (_7f3.places !== 0)) {
                    re = "\\" + _7f3.decimal;
                    if (_7f3.places == Infinity) {
                        re = "(?:" + re + "\\d+)?";
                    } else {
                        re += "\\d{" + _7f3.places + "}";
                    }
                }
                return re;
            }, true);
            var _7f8 = dojo.regexp.buildGroupRE(_7f3.exponent, function (q) {
                if (q) {
                    return "([eE]" + dojo.number._integerRegexp({signed: _7f3.eSigned}) + ")";
                }
                return "";
            });
            var _7fa = _7f4 + _7f5;
            if (_7f5) {
                _7fa = "(?:(?:" + _7fa + ")|(?:" + _7f5 + "))";
            }
            return _7fa + _7f8;
        };
        dojo.number._integerRegexp = function (_7fb) {
            _7fb = _7fb || {};
            if (!("signed" in _7fb)) {
                _7fb.signed = [true, false];
            }
            if (!("separator" in _7fb)) {
                _7fb.separator = "";
            } else {
                if (!("groupSize" in _7fb)) {
                    _7fb.groupSize = 3;
                }
            }
            var _7fc = dojo.regexp.buildGroupRE(_7fb.signed, function (q) {
                return q ? "[-+]" : "";
            }, true);
            var _7fe = dojo.regexp.buildGroupRE(_7fb.separator, function (sep) {
                if (!sep) {
                    return "(?:0|[1-9]\\d*)";
                }
                sep = dojo.regexp.escapeString(sep);
                if (sep == " ") {
                    sep = "\\s";
                } else {
                    if (sep == " ") {
                        sep = "\\s\\xa0";
                    }
                }
                var grp = _7fb.groupSize, grp2 = _7fb.groupSize2;
                if (grp2) {
                    var _802 = "(?:0|[1-9]\\d{0," + (grp2 - 1) + "}(?:[" + sep + "]\\d{" + grp2 + "})*[" + sep + "]\\d{" + grp + "})";
                    return ((grp - grp2) > 0) ? "(?:" + _802 + "|(?:0|[1-9]\\d{0," + (grp - 1) + "}))" : _802;
                }
                return "(?:0|[1-9]\\d{0," + (grp - 1) + "}(?:[" + sep + "]\\d{" + grp + "})*)";
            }, true);
            return _7fc + _7fe;
        };
    }
    if (!dojo._hasResource["dijit.form.NumberTextBox"]) {
        dojo._hasResource["dijit.form.NumberTextBox"] = true;
        dojo.provide("dijit.form.NumberTextBox");
        dojo.declare("dijit.form.NumberTextBoxMixin", null, {regExpGen: dojo.number.regexp, editOptions: {pattern: "#.######"}, _onFocus: function () {
            this._setValueAttr(this.attr("value"), false);
            this.inherited(arguments);
        }, _formatter: dojo.number.format, format: function (_803, _804) {
            if (typeof _803 == "string") {
                return _803;
            }
            if (isNaN(_803)) {
                return "";
            }
            if (this.editOptions && this._focused) {
                _804 = dojo.mixin(dojo.mixin({}, this.editOptions), this.constraints);
            }
            return this._formatter(_803, _804);
        }, parse: dojo.number.parse, filter: function (_805) {
            return (_805 === null || _805 === "" || _805 === undefined) ? NaN : this.inherited(arguments);
        }, serialize: function (_806, _807) {
            return (typeof _806 != "number" || isNaN(_806)) ? "" : this.inherited(arguments);
        }, _getValueAttr: function () {
            var v = this.inherited(arguments);
            if (isNaN(v) && this.textbox.value !== "") {
                return undefined;
            }
            return v;
        }, value: NaN});
        dojo.declare("dijit.form.NumberTextBox", [dijit.form.RangeBoundTextBox, dijit.form.NumberTextBoxMixin], {});
    }
    if (!dojo._hasResource["dojo.cldr.supplemental"]) {
        dojo._hasResource["dojo.cldr.supplemental"] = true;
        dojo.provide("dojo.cldr.supplemental");
        dojo.cldr.supplemental.getFirstDayOfWeek = function (_809) {
            var _80a = {mv: 5, ae: 6, af: 6, bh: 6, dj: 6, dz: 6, eg: 6, er: 6, et: 6, iq: 6, ir: 6, jo: 6, ke: 6, kw: 6, lb: 6, ly: 6, ma: 6, om: 6, qa: 6, sa: 6, sd: 6, so: 6, tn: 6, ye: 6, as: 0, au: 0, az: 0, bw: 0, ca: 0, cn: 0, fo: 0, ge: 0, gl: 0, gu: 0, hk: 0, ie: 0, il: 0, is: 0, jm: 0, jp: 0, kg: 0, kr: 0, la: 0, mh: 0, mo: 0, mp: 0, mt: 0, nz: 0, ph: 0, pk: 0, sg: 0, th: 0, tt: 0, tw: 0, um: 0, us: 0, uz: 0, vi: 0, za: 0, zw: 0, et: 0, mw: 0, ng: 0, tj: 0, sy: 4};
            var _80b = dojo.cldr.supplemental._region(_809);
            var dow = _80a[_80b];
            return (dow === undefined) ? 1 : dow;
        };
        dojo.cldr.supplemental._region = function (_80d) {
            _80d = dojo.i18n.normalizeLocale(_80d);
            var tags = _80d.split("-");
            var _80f = tags[1];
            if (!_80f) {
                _80f = {de: "de", en: "us", es: "es", fi: "fi", fr: "fr", he: "il", hu: "hu", it: "it", ja: "jp", ko: "kr", nl: "nl", pt: "br", sv: "se", zh: "cn"}[tags[0]];
            } else {
                if (_80f.length == 4) {
                    _80f = tags[2];
                }
            }
            return _80f;
        };
        dojo.cldr.supplemental.getWeekend = function (_810) {
            var _811 = {eg: 5, il: 5, sy: 5, "in": 0, ae: 4, bh: 4, dz: 4, iq: 4, jo: 4, kw: 4, lb: 4, ly: 4, ma: 4, om: 4, qa: 4, sa: 4, sd: 4, tn: 4, ye: 4};
            var _812 = {ae: 5, bh: 5, dz: 5, iq: 5, jo: 5, kw: 5, lb: 5, ly: 5, ma: 5, om: 5, qa: 5, sa: 5, sd: 5, tn: 5, ye: 5, af: 5, ir: 5, eg: 6, il: 6, sy: 6};
            var _813 = dojo.cldr.supplemental._region(_810);
            var _814 = _811[_813];
            var end = _812[_813];
            if (_814 === undefined) {
                _814 = 6;
            }
            if (end === undefined) {
                end = 0;
            }
            return {start: _814, end: end};
        };
    }
    if (!dojo._hasResource["dojo.date"]) {
        dojo._hasResource["dojo.date"] = true;
        dojo.provide("dojo.date");
        dojo.date.getDaysInMonth = function (_816) {
            var _817 = _816.getMonth();
            var days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
            if (_817 == 1 && dojo.date.isLeapYear(_816)) {
                return 29;
            }
            return days[_817];
        };
        dojo.date.isLeapYear = function (_819) {
            var year = _819.getFullYear();
            return !(year % 400) || (!(year % 4) && !!(year % 100));
        };
        dojo.date.getTimezoneName = function (_81b) {
            var str = _81b.toString();
            var tz = "";
            var _81e;
            var pos = str.indexOf("(");
            if (pos > -1) {
                tz = str.substring(++pos, str.indexOf(")"));
            } else {
                var pat = /([A-Z\/]+) \d{4}$/;
                if ((_81e = str.match(pat))) {
                    tz = _81e[1];
                } else {
                    str = _81b.toLocaleString();
                    pat = / ([A-Z\/]+)$/;
                    if ((_81e = str.match(pat))) {
                        tz = _81e[1];
                    }
                }
            }
            return (tz == "AM" || tz == "PM") ? "" : tz;
        };
        dojo.date.compare = function (_821, _822, _823) {
            _821 = new Date(Number(_821));
            _822 = new Date(Number(_822 || new Date()));
            if (_823 !== "undefined") {
                if (_823 == "date") {
                    _821.setHours(0, 0, 0, 0);
                    _822.setHours(0, 0, 0, 0);
                } else {
                    if (_823 == "time") {
                        _821.setFullYear(0, 0, 0);
                        _822.setFullYear(0, 0, 0);
                    }
                }
            }
            if (_821 > _822) {
                return 1;
            }
            if (_821 < _822) {
                return -1;
            }
            return 0;
        };
        dojo.date.add = function (date, _825, _826) {
            var sum = new Date(Number(date));
            var _828 = false;
            var _829 = "Date";
            switch (_825) {
                case "day":
                    break;
                case "weekday":
                    var days, _82b;
                    var mod = _826 % 5;
                    if (!mod) {
                        days = (_826 > 0) ? 5 : -5;
                        _82b = (_826 > 0) ? ((_826 - 5) / 5) : ((_826 + 5) / 5);
                    } else {
                        days = mod;
                        _82b = parseInt(_826 / 5);
                    }
                    var strt = date.getDay();
                    var adj = 0;
                    if (strt == 6 && _826 > 0) {
                        adj = 1;
                    } else {
                        if (strt == 0 && _826 < 0) {
                            adj = -1;
                        }
                    }
                    var trgt = strt + days;
                    if (trgt == 0 || trgt == 6) {
                        adj = (_826 > 0) ? 2 : -2;
                    }
                    _826 = (7 * _82b) + days + adj;
                    break;
                case "year":
                    _829 = "FullYear";
                    _828 = true;
                    break;
                case "week":
                    _826 *= 7;
                    break;
                case "quarter":
                    _826 *= 3;
                case "month":
                    _828 = true;
                    _829 = "Month";
                    break;
                case "hour":
                case "minute":
                case "second":
                case "millisecond":
                    _829 = "UTC" + _825.charAt(0).toUpperCase() + _825.substring(1) + "s";
            }
            if (_829) {
                sum["set" + _829](sum["get" + _829]() + _826);
            }
            if (_828 && (sum.getDate() < date.getDate())) {
                sum.setDate(0);
            }
            return sum;
        };
        dojo.date.difference = function (_830, _831, _832) {
            _831 = _831 || new Date();
            _832 = _832 || "day";
            var _833 = _831.getFullYear() - _830.getFullYear();
            var _834 = 1;
            switch (_832) {
                case "quarter":
                    var m1 = _830.getMonth();
                    var m2 = _831.getMonth();
                    var q1 = Math.floor(m1 / 3) + 1;
                    var q2 = Math.floor(m2 / 3) + 1;
                    q2 += (_833 * 4);
                    _834 = q2 - q1;
                    break;
                case "weekday":
                    var days = Math.round(dojo.date.difference(_830, _831, "day"));
                    var _83a = parseInt(dojo.date.difference(_830, _831, "week"));
                    var mod = days % 7;
                    if (mod == 0) {
                        days = _83a * 5;
                    } else {
                        var adj = 0;
                        var aDay = _830.getDay();
                        var bDay = _831.getDay();
                        _83a = parseInt(days / 7);
                        mod = days % 7;
                        var _83f = new Date(_830);
                        _83f.setDate(_83f.getDate() + (_83a * 7));
                        var _840 = _83f.getDay();
                        if (days > 0) {
                            switch (true) {
                                case aDay == 6:
                                    adj = -1;
                                    break;
                                case aDay == 0:
                                    adj = 0;
                                    break;
                                case bDay == 6:
                                    adj = -1;
                                    break;
                                case bDay == 0:
                                    adj = -2;
                                    break;
                                case (_840 + mod) > 5:
                                    adj = -2;
                            }
                        } else {
                            if (days < 0) {
                                switch (true) {
                                    case aDay == 6:
                                        adj = 0;
                                        break;
                                    case aDay == 0:
                                        adj = 1;
                                        break;
                                    case bDay == 6:
                                        adj = 2;
                                        break;
                                    case bDay == 0:
                                        adj = 1;
                                        break;
                                    case (_840 + mod) < 0:
                                        adj = 2;
                                }
                            }
                        }
                        days += adj;
                        days -= (_83a * 2);
                    }
                    _834 = days;
                    break;
                case "year":
                    _834 = _833;
                    break;
                case "month":
                    _834 = (_831.getMonth() - _830.getMonth()) + (_833 * 12);
                    break;
                case "week":
                    _834 = parseInt(dojo.date.difference(_830, _831, "day") / 7);
                    break;
                case "day":
                    _834 /= 24;
                case "hour":
                    _834 /= 60;
                case "minute":
                    _834 /= 60;
                case "second":
                    _834 /= 1000;
                case "millisecond":
                    _834 *= _831.getTime() - _830.getTime();
            }
            return Math.round(_834);
        };
    }
    if (!dojo._hasResource["dojo.date.locale"]) {
        dojo._hasResource["dojo.date.locale"] = true;
        dojo.provide("dojo.date.locale");
        (function () {
            function formatPattern(_841, _842, _843, _844) {
                return _844.replace(/([a-z])\1*/ig, function (_845) {
                    var s, pad;
                    var c = _845.charAt(0);
                    var l = _845.length;
                    var _84a = ["abbr", "wide", "narrow"];
                    switch (c) {
                        case "G":
                            s = _842[(l < 4) ? "eraAbbr" : "eraNames"][_841.getFullYear() < 0 ? 0 : 1];
                            break;
                        case "y":
                            s = _841.getFullYear();
                            switch (l) {
                                case 1:
                                    break;
                                case 2:
                                    if (!_843) {
                                        s = String(s);
                                        s = s.substr(s.length - 2);
                                        break;
                                    }
                                default:
                                    pad = true;
                            }
                            break;
                        case "Q":
                        case "q":
                            s = Math.ceil((_841.getMonth() + 1) / 3);
                            pad = true;
                            break;
                        case "M":
                            var m = _841.getMonth();
                            if (l < 3) {
                                s = m + 1;
                                pad = true;
                            } else {
                                var _84c = ["months", "format", _84a[l - 3]].join("-");
                                s = _842[_84c][m];
                            }
                            break;
                        case "w":
                            var _84d = 0;
                            s = dojo.date.locale._getWeekOfYear(_841, _84d);
                            pad = true;
                            break;
                        case "d":
                            s = _841.getDate();
                            pad = true;
                            break;
                        case "D":
                            s = dojo.date.locale._getDayOfYear(_841);
                            pad = true;
                            break;
                        case "E":
                            var d = _841.getDay();
                            if (l < 3) {
                                s = d + 1;
                                pad = true;
                            } else {
                                var _84f = ["days", "format", _84a[l - 3]].join("-");
                                s = _842[_84f][d];
                            }
                            break;
                        case "a":
                            var _850 = (_841.getHours() < 12) ? "am" : "pm";
                            s = _842[_850];
                            break;
                        case "h":
                        case "H":
                        case "K":
                        case "k":
                            var h = _841.getHours();
                            switch (c) {
                                case "h":
                                    s = (h % 12) || 12;
                                    break;
                                case "H":
                                    s = h;
                                    break;
                                case "K":
                                    s = (h % 12);
                                    break;
                                case "k":
                                    s = h || 24;
                                    break;
                            }
                            pad = true;
                            break;
                        case "m":
                            s = _841.getMinutes();
                            pad = true;
                            break;
                        case "s":
                            s = _841.getSeconds();
                            pad = true;
                            break;
                        case "S":
                            s = Math.round(_841.getMilliseconds() * Math.pow(10, l - 3));
                            pad = true;
                            break;
                        case "v":
                        case "z":
                            s = dojo.date.getTimezoneName(_841);
                            if (s) {
                                break;
                            }
                            l = 4;
                        case "Z":
                            var _852 = _841.getTimezoneOffset();
                            var tz = [(_852 <= 0 ? "+" : "-"), dojo.string.pad(Math.floor(Math.abs(_852) / 60), 2), dojo.string.pad(Math.abs(_852) % 60, 2)];
                            if (l == 4) {
                                tz.splice(0, 0, "GMT");
                                tz.splice(3, 0, ":");
                            }
                            s = tz.join("");
                            break;
                        default:
                            throw new Error("dojo.date.locale.format: invalid pattern char: " + _844);
                    }
                    if (pad) {
                        s = dojo.string.pad(s, l);
                    }
                    return s;
                });
            };
            dojo.date.locale.format = function (_854, _855) {
                _855 = _855 || {};
                var _856 = dojo.i18n.normalizeLocale(_855.locale);
                var _857 = _855.formatLength || "short";
                var _858 = dojo.date.locale._getGregorianBundle(_856);
                var str = [];
                var _85a = dojo.hitch(this, formatPattern, _854, _858, _855.fullYear);
                if (_855.selector == "year") {
                    var year = _854.getFullYear();
                    if (_856.match(/^zh|^ja/)) {
                        year += "年";
                    }
                    return year;
                }
                if (_855.selector != "time") {
                    var _85c = _855.datePattern || _858["dateFormat-" + _857];
                    if (_85c) {
                        str.push(_processPattern(_85c, _85a));
                    }
                }
                if (_855.selector != "date") {
                    var _85d = _855.timePattern || _858["timeFormat-" + _857];
                    if (_85d) {
                        str.push(_processPattern(_85d, _85a));
                    }
                }
                var _85e = str.join(" ");
                return _85e;
            };
            dojo.date.locale.regexp = function (_85f) {
                return dojo.date.locale._parseInfo(_85f).regexp;
            };
            dojo.date.locale._parseInfo = function (_860) {
                _860 = _860 || {};
                var _861 = dojo.i18n.normalizeLocale(_860.locale);
                var _862 = dojo.date.locale._getGregorianBundle(_861);
                var _863 = _860.formatLength || "short";
                var _864 = _860.datePattern || _862["dateFormat-" + _863];
                var _865 = _860.timePattern || _862["timeFormat-" + _863];
                var _866;
                if (_860.selector == "date") {
                    _866 = _864;
                } else {
                    if (_860.selector == "time") {
                        _866 = _865;
                    } else {
                        _866 = _864 + " " + _865;
                    }
                }
                var _867 = [];
                var re = _processPattern(_866, dojo.hitch(this, _buildDateTimeRE, _867, _862, _860));
                return {regexp: re, tokens: _867, bundle: _862};
            };
            dojo.date.locale.parse = function (_869, _86a) {
                var info = dojo.date.locale._parseInfo(_86a);
                var _86c = info.tokens, _86d = info.bundle;
                var re = new RegExp("^" + info.regexp + "$", info.strict ? "" : "i");
                var _86f = re.exec(_869);
                if (!_86f) {
                    return null;
                }
                var _870 = ["abbr", "wide", "narrow"];
                var _871 = [1970, 0, 1, 0, 0, 0, 0];
                var amPm = "";
                var _873 = dojo.every(_86f, function (v, i) {
                    if (!i) {
                        return true;
                    }
                    var _876 = _86c[i - 1];
                    var l = _876.length;
                    switch (_876.charAt(0)) {
                        case "y":
                            if (l != 2 && _86a.strict) {
                                _871[0] = v;
                            } else {
                                if (v < 100) {
                                    v = Number(v);
                                    var year = "" + new Date().getFullYear();
                                    var _879 = year.substring(0, 2) * 100;
                                    var _87a = Math.min(Number(year.substring(2, 4)) + 20, 99);
                                    var num = (v < _87a) ? _879 + v : _879 - 100 + v;
                                    _871[0] = num;
                                } else {
                                    if (_86a.strict) {
                                        return false;
                                    }
                                    _871[0] = v;
                                }
                            }
                            break;
                        case "M":
                            if (l > 2) {
                                var _87c = _86d["months-format-" + _870[l - 3]].concat();
                                if (!_86a.strict) {
                                    v = v.replace(".", "").toLowerCase();
                                    _87c = dojo.map(_87c, function (s) {
                                        return s.replace(".", "").toLowerCase();
                                    });
                                }
                                v = dojo.indexOf(_87c, v);
                                if (v == -1) {
                                    return false;
                                }
                            } else {
                                v--;
                            }
                            _871[1] = v;
                            break;
                        case "E":
                        case "e":
                            var days = _86d["days-format-" + _870[l - 3]].concat();
                            if (!_86a.strict) {
                                v = v.toLowerCase();
                                days = dojo.map(days, function (d) {
                                    return d.toLowerCase();
                                });
                            }
                            v = dojo.indexOf(days, v);
                            if (v == -1) {
                                return false;
                            }
                            break;
                        case "D":
                            _871[1] = 0;
                        case "d":
                            _871[2] = v;
                            break;
                        case "a":
                            var am = _86a.am || _86d.am;
                            var pm = _86a.pm || _86d.pm;
                            if (!_86a.strict) {
                                var _882 = /\./g;
                                v = v.replace(_882, "").toLowerCase();
                                am = am.replace(_882, "").toLowerCase();
                                pm = pm.replace(_882, "").toLowerCase();
                            }
                            if (_86a.strict && v != am && v != pm) {
                                return false;
                            }
                            amPm = (v == pm) ? "p" : (v == am) ? "a" : "";
                            break;
                        case "K":
                            if (v == 24) {
                                v = 0;
                            }
                        case "h":
                        case "H":
                        case "k":
                            if (v > 23) {
                                return false;
                            }
                            _871[3] = v;
                            break;
                        case "m":
                            _871[4] = v;
                            break;
                        case "s":
                            _871[5] = v;
                            break;
                        case "S":
                            _871[6] = v;
                    }
                    return true;
                });
                var _883 = +_871[3];
                if (amPm === "p" && _883 < 12) {
                    _871[3] = _883 + 12;
                } else {
                    if (amPm === "a" && _883 == 12) {
                        _871[3] = 0;
                    }
                }
                var _884 = new Date(_871[0], _871[1], _871[2], _871[3], _871[4], _871[5], _871[6]);
                if (_86a.strict) {
                    _884.setFullYear(_871[0]);
                }
                var _885 = _86c.join("");
                if (!_873 || (_885.indexOf("M") != -1 && _884.getMonth() != _871[1]) || (_885.indexOf("d") != -1 && _884.getDate() != _871[2])) {
                    return null;
                }
                return _884;
            };
            function _processPattern(_886, _887, _888, _889) {
                var _88a = function (x) {
                    return x;
                };
                _887 = _887 || _88a;
                _888 = _888 || _88a;
                _889 = _889 || _88a;
                var _88c = _886.match(/(''|[^'])+/g);
                var _88d = _886.charAt(0) == "'";
                dojo.forEach(_88c, function (_88e, i) {
                    if (!_88e) {
                        _88c[i] = "";
                    } else {
                        _88c[i] = (_88d ? _888 : _887)(_88e);
                        _88d = !_88d;
                    }
                });
                return _889(_88c.join(""));
            };
            function _buildDateTimeRE(_890, _891, _892, _893) {
                _893 = dojo.regexp.escapeString(_893);
                if (!_892.strict) {
                    _893 = _893.replace(" a", " ?a");
                }
                return _893.replace(/([a-z])\1*/ig,function (_894) {
                    var s;
                    var c = _894.charAt(0);
                    var l = _894.length;
                    var p2 = "", p3 = "";
                    if (_892.strict) {
                        if (l > 1) {
                            p2 = "0" + "{" + (l - 1) + "}";
                        }
                        if (l > 2) {
                            p3 = "0" + "{" + (l - 2) + "}";
                        }
                    } else {
                        p2 = "0?";
                        p3 = "0{0,2}";
                    }
                    switch (c) {
                        case "y":
                            s = "\\d{2,4}";
                            break;
                        case "M":
                            s = (l > 2) ? "\\S+?" : p2 + "[1-9]|1[0-2]";
                            break;
                        case "D":
                            s = p2 + "[1-9]|" + p3 + "[1-9][0-9]|[12][0-9][0-9]|3[0-5][0-9]|36[0-6]";
                            break;
                        case "d":
                            s = "[12]\\d|" + p2 + "[1-9]|3[01]";
                            break;
                        case "w":
                            s = p2 + "[1-9]|[1-4][0-9]|5[0-3]";
                            break;
                        case "E":
                            s = "\\S+";
                            break;
                        case "h":
                            s = p2 + "[1-9]|1[0-2]";
                            break;
                        case "k":
                            s = p2 + "\\d|1[01]";
                            break;
                        case "H":
                            s = p2 + "\\d|1\\d|2[0-3]";
                            break;
                        case "K":
                            s = p2 + "[1-9]|1\\d|2[0-4]";
                            break;
                        case "m":
                        case "s":
                            s = "[0-5]\\d";
                            break;
                        case "S":
                            s = "\\d{" + l + "}";
                            break;
                        case "a":
                            var am = _892.am || _891.am || "AM";
                            var pm = _892.pm || _891.pm || "PM";
                            if (_892.strict) {
                                s = am + "|" + pm;
                            } else {
                                s = am + "|" + pm;
                                if (am != am.toLowerCase()) {
                                    s += "|" + am.toLowerCase();
                                }
                                if (pm != pm.toLowerCase()) {
                                    s += "|" + pm.toLowerCase();
                                }
                                if (s.indexOf(".") != -1) {
                                    s += "|" + s.replace(/\./g, "");
                                }
                            }
                            s = s.replace(/\./g, "\\.");
                            break;
                        default:
                            s = ".*";
                    }
                    if (_890) {
                        _890.push(_894);
                    }
                    return "(" + s + ")";
                }).replace(/[\xa0 ]/g, "[\\s\\xa0]");
            };
        })();
        (function () {
            var _89c = [];
            dojo.date.locale.addCustomFormats = function (_89d, _89e) {
                _89c.push({pkg: _89d, name: _89e});
            };
            dojo.date.locale._getGregorianBundle = function (_89f) {
                var _8a0 = {};
                dojo.forEach(_89c, function (desc) {
                    var _8a2 = dojo.i18n.getLocalization(desc.pkg, desc.name, _89f);
                    _8a0 = dojo.mixin(_8a0, _8a2);
                }, this);
                return _8a0;
            };
        })();
        dojo.date.locale.addCustomFormats("dojo.cldr", "gregorian");
        dojo.date.locale.getNames = function (item, type, use, _8a6) {
            var _8a7;
            var _8a8 = dojo.date.locale._getGregorianBundle(_8a6);
            var _8a9 = [item, use, type];
            if (use == "standAlone") {
                var key = _8a9.join("-");
                _8a7 = _8a8[key];
                if (_8a7[0] == 1) {
                    _8a7 = undefined;
                }
            }
            _8a9[1] = "format";
            return (_8a7 || _8a8[_8a9.join("-")]).concat();
        };
        dojo.date.locale.isWeekend = function (_8ab, _8ac) {
            var _8ad = dojo.cldr.supplemental.getWeekend(_8ac);
            var day = (_8ab || new Date()).getDay();
            if (_8ad.end < _8ad.start) {
                _8ad.end += 7;
                if (day < _8ad.start) {
                    day += 7;
                }
            }
            return day >= _8ad.start && day <= _8ad.end;
        };
        dojo.date.locale._getDayOfYear = function (_8af) {
            return dojo.date.difference(new Date(_8af.getFullYear(), 0, 1, _8af.getHours()), _8af) + 1;
        };
        dojo.date.locale._getWeekOfYear = function (_8b0, _8b1) {
            if (arguments.length == 1) {
                _8b1 = 0;
            }
            var _8b2 = new Date(_8b0.getFullYear(), 0, 1).getDay();
            var adj = (_8b2 - _8b1 + 7) % 7;
            var week = Math.floor((dojo.date.locale._getDayOfYear(_8b0) + adj - 1) / 7);
            if (_8b2 == _8b1) {
                week++;
            }
            return week;
        };
    }
    if (!dojo._hasResource["dijit._Calendar"]) {
        dojo._hasResource["dijit._Calendar"] = true;
        dojo.provide("dijit._Calendar");
        dojo.declare("dijit._Calendar", [dijit._Widget, dijit._Templated], {templateString: "<table cellspacing=\"0\" cellpadding=\"0\" class=\"dijitCalendarContainer\">\n\t<thead>\n\t\t<tr class=\"dijitReset dijitCalendarMonthContainer\" valign=\"top\">\n\t\t\t<th class='dijitReset' dojoAttachPoint=\"decrementMonth\">\n\t\t\t\t<div class=\"dijitInline dijitCalendarIncrementControl dijitCalendarDecrease\"><span dojoAttachPoint=\"decreaseArrowNode\" class=\"dijitA11ySideArrow dijitCalendarIncrementControl dijitCalendarDecreaseInner\">-</span></div>\n\t\t\t</th>\n\t\t\t<th class='dijitReset' colspan=\"5\">\n\t\t\t\t<div dojoAttachPoint=\"monthLabelSpacer\" class=\"dijitCalendarMonthLabelSpacer\"></div>\n\t\t\t\t<div dojoAttachPoint=\"monthLabelNode\" class=\"dijitCalendarMonthLabel\"></div>\n\t\t\t</th>\n\t\t\t<th class='dijitReset' dojoAttachPoint=\"incrementMonth\">\n\t\t\t\t<div class=\"dijitInline dijitCalendarIncrementControl dijitCalendarIncrease\"><span dojoAttachPoint=\"increaseArrowNode\" class=\"dijitA11ySideArrow dijitCalendarIncrementControl dijitCalendarIncreaseInner\">+</span></div>\n\t\t\t</th>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th class=\"dijitReset dijitCalendarDayLabelTemplate\"><span class=\"dijitCalendarDayLabel\"></span></th>\n\t\t</tr>\n\t</thead>\n\t<tbody dojoAttachEvent=\"onclick: _onDayClick, onmouseover: _onDayMouseOver, onmouseout: _onDayMouseOut\" class=\"dijitReset dijitCalendarBodyContainer\">\n\t\t<tr class=\"dijitReset dijitCalendarWeekTemplate\">\n\t\t\t<td class=\"dijitReset dijitCalendarDateTemplate\"><span class=\"dijitCalendarDateLabel\"></span></td>\n\t\t</tr>\n\t</tbody>\n\t<tfoot class=\"dijitReset dijitCalendarYearContainer\">\n\t\t<tr>\n\t\t\t<td class='dijitReset' valign=\"top\" colspan=\"7\">\n\t\t\t\t<h3 class=\"dijitCalendarYearLabel\">\n\t\t\t\t\t<span dojoAttachPoint=\"previousYearLabelNode\" class=\"dijitInline dijitCalendarPreviousYear\"></span>\n\t\t\t\t\t<span dojoAttachPoint=\"currentYearLabelNode\" class=\"dijitInline dijitCalendarSelectedYear\"></span>\n\t\t\t\t\t<span dojoAttachPoint=\"nextYearLabelNode\" class=\"dijitInline dijitCalendarNextYear\"></span>\n\t\t\t\t</h3>\n\t\t\t</td>\n\t\t</tr>\n\t</tfoot>\n</table>\t\n", value: new Date(), dayWidth: "narrow", setValue: function (_8b5) {
            dojo.deprecated("dijit.Calendar:setValue() is deprecated.  Use attr('value', ...) instead.", "", "2.0");
            this.attr("value", _8b5);
        }, _setValueAttr: function (_8b6) {
            if (!this.value || dojo.date.compare(_8b6, this.value)) {
                _8b6 = new Date(_8b6);
                this.displayMonth = new Date(_8b6);
                if (!this.isDisabledDate(_8b6, this.lang)) {
                    this.value = _8b6;
                    this.value.setHours(0, 0, 0, 0);
                    this.onChange(this.value);
                }
                this._populateGrid();
            }
        }, _setText: function (node, text) {
            while (node.firstChild) {
                node.removeChild(node.firstChild);
            }
            node.appendChild(dojo.doc.createTextNode(text));
        }, _populateGrid: function () {
            var _8b9 = this.displayMonth;
            _8b9.setDate(1);
            var _8ba = _8b9.getDay();
            var _8bb = dojo.date.getDaysInMonth(_8b9);
            var _8bc = dojo.date.getDaysInMonth(dojo.date.add(_8b9, "month", -1));
            var _8bd = new Date();
            var _8be = this.value;
            var _8bf = dojo.cldr.supplemental.getFirstDayOfWeek(this.lang);
            if (_8bf > _8ba) {
                _8bf -= 7;
            }
            dojo.query(".dijitCalendarDateTemplate", this.domNode).forEach(function (_8c0, i) {
                i += _8bf;
                var date = new Date(_8b9);
                var _8c3, _8c4 = "dijitCalendar", adj = 0;
                if (i < _8ba) {
                    _8c3 = _8bc - _8ba + i + 1;
                    adj = -1;
                    _8c4 += "Previous";
                } else {
                    if (i >= (_8ba + _8bb)) {
                        _8c3 = i - _8ba - _8bb + 1;
                        adj = 1;
                        _8c4 += "Next";
                    } else {
                        _8c3 = i - _8ba + 1;
                        _8c4 += "Current";
                    }
                }
                if (adj) {
                    date = dojo.date.add(date, "month", adj);
                }
                date.setDate(_8c3);
                if (!dojo.date.compare(date, _8bd, "date")) {
                    _8c4 = "dijitCalendarCurrentDate " + _8c4;
                }
                if (!dojo.date.compare(date, _8be, "date")) {
                    _8c4 = "dijitCalendarSelectedDate " + _8c4;
                }
                if (this.isDisabledDate(date, this.lang)) {
                    _8c4 = "dijitCalendarDisabledDate " + _8c4;
                }
                var _8c6 = this.getClassForDate(date, this.lang);
                if (_8c6) {
                    _8c4 = _8c6 + " " + _8c4;
                }
                _8c0.className = _8c4 + "Month dijitCalendarDateTemplate";
                _8c0.dijitDateValue = date.valueOf();
                var _8c7 = dojo.query(".dijitCalendarDateLabel", _8c0)[0];
                this._setText(_8c7, date.getDate());
            }, this);
            var _8c8 = dojo.date.locale.getNames("months", "wide", "standAlone", this.lang);
            this._setText(this.monthLabelNode, _8c8[_8b9.getMonth()]);
            var y = _8b9.getFullYear() - 1;
            var d = new Date();
            dojo.forEach(["previous", "current", "next"], function (name) {
                d.setFullYear(y++);
                this._setText(this[name + "YearLabelNode"], dojo.date.locale.format(d, {selector: "year", locale: this.lang}));
            }, this);
            var _8cc = this;
            var _8cd = function (_8ce, _8cf, adj) {
                _8cc._connects.push(dijit.typematic.addMouseListener(_8cc[_8ce], _8cc, function (_8d1) {
                    if (_8d1 >= 0) {
                        _8cc._adjustDisplay(_8cf, adj);
                    }
                }, 0.8, 500));
            };
            _8cd("incrementMonth", "month", 1);
            _8cd("decrementMonth", "month", -1);
            _8cd("nextYearLabelNode", "year", 1);
            _8cd("previousYearLabelNode", "year", -1);
        }, goToToday: function () {
            this.attr("value", new Date());
        }, postCreate: function () {
            this.inherited(arguments);
            var _8d2 = dojo.hitch(this, function (_8d3, n) {
                var _8d5 = dojo.query(_8d3, this.domNode)[0];
                for (var i = 0; i < n; i++) {
                    _8d5.parentNode.appendChild(_8d5.cloneNode(true));
                }
            });
            _8d2(".dijitCalendarDayLabelTemplate", 6);
            _8d2(".dijitCalendarDateTemplate", 6);
            _8d2(".dijitCalendarWeekTemplate", 5);
            var _8d7 = dojo.date.locale.getNames("days", this.dayWidth, "standAlone", this.lang);
            var _8d8 = dojo.cldr.supplemental.getFirstDayOfWeek(this.lang);
            dojo.query(".dijitCalendarDayLabel", this.domNode).forEach(function (_8d9, i) {
                this._setText(_8d9, _8d7[(i + _8d8) % 7]);
            }, this);
            var _8db = dojo.date.locale.getNames("months", "wide", "standAlone", this.lang);
            dojo.forEach(_8db, function (name) {
                var _8dd = dojo.doc.createElement("div");
                this._setText(_8dd, name);
                this.monthLabelSpacer.appendChild(_8dd);
            }, this);
            this.value = null;
            this.attr("value", new Date());
        }, _adjustDisplay: function (part, _8df) {
            this.displayMonth = dojo.date.add(this.displayMonth, part, _8df);
            this._populateGrid();
        }, _onDayClick: function (evt) {
            var node = evt.target;
            dojo.stopEvent(evt);
            while (!node.dijitDateValue) {
                node = node.parentNode;
            }
            if (!dojo.hasClass(node, "dijitCalendarDisabledDate")) {
                this.attr("value", node.dijitDateValue);
                this.onValueSelected(this.value);
            }
        }, _onDayMouseOver: function (evt) {
            var node = evt.target;
            if (node && (node.dijitDateValue || node == this.previousYearLabelNode || node == this.nextYearLabelNode)) {
                dojo.addClass(node, "dijitCalendarHoveredDate");
                this._currentNode = node;
            }
        }, _onDayMouseOut: function (evt) {
            if (!this._currentNode) {
                return;
            }
            for (var node = evt.relatedTarget; node;) {
                if (node == this._currentNode) {
                    return;
                }
                try {
                    node = node.parentNode;
                } catch (x) {
                    node = null;
                }
            }
            dojo.removeClass(this._currentNode, "dijitCalendarHoveredDate");
            this._currentNode = null;
        }, onValueSelected: function (date) {
        }, onChange: function (date) {
        }, isDisabledDate: function (_8e8, _8e9) {
        }, getClassForDate: function (_8ea, _8eb) {
        }});
    }
    if (!dojo._hasResource["dijit.form._DateTimeTextBox"]) {
        dojo._hasResource["dijit.form._DateTimeTextBox"] = true;
        dojo.provide("dijit.form._DateTimeTextBox");
        dojo.declare("dijit.form._DateTimeTextBox", dijit.form.RangeBoundTextBox, {regExpGen: dojo.date.locale.regexp, compare: dojo.date.compare, format: function (_8ec, _8ed) {
            if (!_8ec) {
                return "";
            }
            return dojo.date.locale.format(_8ec, _8ed);
        }, parse: function (_8ee, _8ef) {
            return dojo.date.locale.parse(_8ee, _8ef) || (this._isEmpty(_8ee) ? null : undefined);
        }, serialize: dojo.date.stamp.toISOString, value: new Date(""), popupClass: "", _selector: "", postMixInProperties: function () {
            this.inherited(arguments);
            if (!this.value || this.value.toString() == dijit.form._DateTimeTextBox.prototype.value.toString()) {
                this.value = null;
            }
            var _8f0 = this.constraints;
            _8f0.selector = this._selector;
            _8f0.fullYear = true;
            var _8f1 = dojo.date.stamp.fromISOString;
            if (typeof _8f0.min == "string") {
                _8f0.min = _8f1(_8f0.min);
            }
            if (typeof _8f0.max == "string") {
                _8f0.max = _8f1(_8f0.max);
            }
        }, _onFocus: function (evt) {
            this._open();
        }, _setValueAttr: function (_8f3, _8f4, _8f5) {
            this.inherited(arguments);
            if (this._picker) {
                if (!_8f3) {
                    _8f3 = new Date();
                }
                this._picker.attr("value", _8f3);
            }
        }, _open: function () {
            if (this.disabled || this.readOnly || !this.popupClass) {
                return;
            }
            var _8f6 = this;
            if (!this._picker) {
                var _8f7 = dojo.getObject(this.popupClass, false);
                this._picker = new _8f7({onValueSelected: function (_8f8) {
                    if (_8f6._tabbingAway) {
                        delete _8f6._tabbingAway;
                    } else {
                        _8f6.focus();
                    }
                    setTimeout(dojo.hitch(_8f6, "_close"), 1);
                    dijit.form._DateTimeTextBox.superclass._setValueAttr.call(_8f6, _8f8, true);
                }, lang: _8f6.lang, constraints: _8f6.constraints, isDisabledDate: function (date) {
                    var _8fa = dojo.date.compare;
                    var _8fb = _8f6.constraints;
                    return _8fb && (_8fb.min && (_8fa(_8fb.min, date, "date") > 0) || (_8fb.max && _8fa(_8fb.max, date, "date") < 0));
                }});
                this._picker.attr("value", this.attr("value") || new Date());
            }
            if (!this._opened) {
                dijit.popup.open({parent: this, popup: this._picker, around: this.domNode, onCancel: dojo.hitch(this, this._close), onClose: function () {
                    _8f6._opened = false;
                }});
                this._opened = true;
            }
            dojo.marginBox(this._picker.domNode, {w: this.domNode.offsetWidth});
        }, _close: function () {
            if (this._opened) {
                dijit.popup.close(this._picker);
                this._opened = false;
            }
        }, _onBlur: function () {
            this._close();
            if (this._picker) {
                this._picker.destroy();
                delete this._picker;
            }
            this.inherited(arguments);
        }, _getDisplayedValueAttr: function () {
            return this.textbox.value;
        }, _setDisplayedValueAttr: function (_8fc, _8fd) {
            this._setValueAttr(this.parse(_8fc, this.constraints), _8fd, _8fc);
        }, destroy: function () {
            if (this._picker) {
                this._picker.destroy();
                delete this._picker;
            }
            this.inherited(arguments);
        }, _onKeyPress: function (e) {
            var p = this._picker, dk = dojo.keys;
            if (p && this._opened && p.handleKey) {
                if (p.handleKey(e) === false) {
                    return;
                }
            }
            if (this._opened && e.charOrCode == dk.ESCAPE && !e.shiftKey && !e.ctrlKey && !e.altKey) {
                this._close();
                dojo.stopEvent(e);
            } else {
                if (!this._opened && e.charOrCode == dk.DOWN_ARROW) {
                    this._open();
                    dojo.stopEvent(e);
                } else {
                    if (dijit.form._DateTimeTextBox.superclass._onKeyPress.apply(this, arguments)) {
                        if (e.charOrCode === dk.TAB) {
                            this._tabbingAway = true;
                        } else {
                            if (this._opened && (e.keyChar || e.charOrCode === dk.BACKSPACE || e.charOrCode == dk.DELETE)) {
                                setTimeout(dojo.hitch(this, function () {
                                    dijit.placeOnScreenAroundElement(p.domNode.parentNode, this.domNode, {"BL": "TL", "TL": "BL"}, p.orient ? dojo.hitch(p, "orient") : null);
                                }), 1);
                            }
                        }
                    }
                }
            }
        }});
    }
    if (!dojo._hasResource["dijit.form.DateTextBox"]) {
        dojo._hasResource["dijit.form.DateTextBox"] = true;
        dojo.provide("dijit.form.DateTextBox");
        dojo.declare("dijit.form.DateTextBox", dijit.form._DateTimeTextBox, {baseClass: "dijitTextBox dijitDateTextBox", popupClass: "dijit._Calendar", _selector: "date"});
    }
    if (!dojo._hasResource["dojo.cldr.monetary"]) {
        dojo._hasResource["dojo.cldr.monetary"] = true;
        dojo.provide("dojo.cldr.monetary");
        dojo.cldr.monetary.getData = function (code) {
            var _902 = {ADP: 0, BHD: 3, BIF: 0, BYR: 0, CLF: 0, CLP: 0, DJF: 0, ESP: 0, GNF: 0, IQD: 3, ITL: 0, JOD: 3, JPY: 0, KMF: 0, KRW: 0, KWD: 3, LUF: 0, LYD: 3, MGA: 0, MGF: 0, OMR: 3, PYG: 0, RWF: 0, TND: 3, TRL: 0, VUV: 0, XAF: 0, XOF: 0, XPF: 0};
            var _903 = {CHF: 5};
            var _904 = _902[code], _905 = _903[code];
            if (typeof _904 == "undefined") {
                _904 = 2;
            }
            if (typeof _905 == "undefined") {
                _905 = 0;
            }
            return {places: _904, round: _905};
        };
    }
    if (!dojo._hasResource["dojo.currency"]) {
        dojo._hasResource["dojo.currency"] = true;
        dojo.provide("dojo.currency");
        dojo.currency._mixInDefaults = function (_906) {
            _906 = _906 || {};
            _906.type = "currency";
            var _907 = dojo.i18n.getLocalization("dojo.cldr", "currency", _906.locale) || {};
            var iso = _906.currency;
            var data = dojo.cldr.monetary.getData(iso);
            dojo.forEach(["displayName", "symbol", "group", "decimal"], function (prop) {
                data[prop] = _907[iso + "_" + prop];
            });
            data.fractional = [true, false];
            return dojo.mixin(data, _906);
        };
        dojo.currency.format = function (_90b, _90c) {
            return dojo.number.format(_90b, dojo.currency._mixInDefaults(_90c));
        };
        dojo.currency.regexp = function (_90d) {
            return dojo.number.regexp(dojo.currency._mixInDefaults(_90d));
        };
        dojo.currency.parse = function (_90e, _90f) {
            return dojo.number.parse(_90e, dojo.currency._mixInDefaults(_90f));
        };
    }
    if (!dojo._hasResource["dijit.form.CurrencyTextBox"]) {
        dojo._hasResource["dijit.form.CurrencyTextBox"] = true;
        dojo.provide("dijit.form.CurrencyTextBox");
        dojo.declare("dijit.form.CurrencyTextBox", dijit.form.NumberTextBox, {currency: "", regExpGen: dojo.currency.regexp, _formatter: dojo.currency.format, parse: dojo.currency.parse, postMixInProperties: function () {
            if (this.constraints === dijit.form.ValidationTextBox.prototype.constraints) {
                this.constraints = {};
            }
            this.constraints.currency = this.currency;
            dijit.form.CurrencyTextBox.superclass.postMixInProperties.apply(this, arguments);
        }});
    }
    if (!dojo._hasResource["dojo.dnd.common"]) {
        dojo._hasResource["dojo.dnd.common"] = true;
        dojo.provide("dojo.dnd.common");
        dojo.dnd._isMac = navigator.appVersion.indexOf("Macintosh") >= 0;
        dojo.dnd._copyKey = dojo.dnd._isMac ? "metaKey" : "ctrlKey";
        dojo.dnd.getCopyKeyState = function (e) {
            return e[dojo.dnd._copyKey];
        };
        dojo.dnd._uniqueId = 0;
        dojo.dnd.getUniqueId = function () {
            var id;
            do {
                id = dojo._scopeName + "Unique" + (++dojo.dnd._uniqueId);
            } while (dojo.byId(id));
            return id;
        };
        dojo.dnd._empty = {};
        dojo.dnd.isFormElement = function (e) {
            var t = e.target;
            if (t.nodeType == 3) {
                t = t.parentNode;
            }
            return " button textarea input select option ".indexOf(" " + t.tagName.toLowerCase() + " ") >= 0;
        };
    }
    if (!dojo._hasResource["dojo.dnd.autoscroll"]) {
        dojo._hasResource["dojo.dnd.autoscroll"] = true;
        dojo.provide("dojo.dnd.autoscroll");
        dojo.dnd.getViewport = function () {
            var d = dojo.doc, dd = d.documentElement, w = window, b = dojo.body();
            if (dojo.isMozilla) {
                return {w: dd.clientWidth, h: w.innerHeight};
            } else {
                if (!dojo.isOpera && w.innerWidth) {
                    return {w: w.innerWidth, h: w.innerHeight};
                } else {
                    if (!dojo.isOpera && dd && dd.clientWidth) {
                        return {w: dd.clientWidth, h: dd.clientHeight};
                    } else {
                        if (b.clientWidth) {
                            return {w: b.clientWidth, h: b.clientHeight};
                        }
                    }
                }
            }
            return null;
        };
        dojo.dnd.V_TRIGGER_AUTOSCROLL = 32;
        dojo.dnd.H_TRIGGER_AUTOSCROLL = 32;
        dojo.dnd.V_AUTOSCROLL_VALUE = 16;
        dojo.dnd.H_AUTOSCROLL_VALUE = 16;
        dojo.dnd.autoScroll = function (e) {
            var v = dojo.dnd.getViewport(), dx = 0, dy = 0;
            if (e.clientX < dojo.dnd.H_TRIGGER_AUTOSCROLL) {
                dx = -dojo.dnd.H_AUTOSCROLL_VALUE;
            } else {
                if (e.clientX > v.w - dojo.dnd.H_TRIGGER_AUTOSCROLL) {
                    dx = dojo.dnd.H_AUTOSCROLL_VALUE;
                }
            }
            if (e.clientY < dojo.dnd.V_TRIGGER_AUTOSCROLL) {
                dy = -dojo.dnd.V_AUTOSCROLL_VALUE;
            } else {
                if (e.clientY > v.h - dojo.dnd.V_TRIGGER_AUTOSCROLL) {
                    dy = dojo.dnd.V_AUTOSCROLL_VALUE;
                }
            }
            window.scrollBy(dx, dy);
        };
        dojo.dnd._validNodes = {"div": 1, "p": 1, "td": 1};
        dojo.dnd._validOverflow = {"auto": 1, "scroll": 1};
        dojo.dnd.autoScrollNodes = function (e) {
            for (var n = e.target; n;) {
                if (n.nodeType == 1 && (n.tagName.toLowerCase() in dojo.dnd._validNodes)) {
                    var s = dojo.getComputedStyle(n);
                    if (s.overflow.toLowerCase() in dojo.dnd._validOverflow) {
                        var b = dojo._getContentBox(n, s), t = dojo._abs(n, true);
                        var w = Math.min(dojo.dnd.H_TRIGGER_AUTOSCROLL, b.w / 2), h = Math.min(dojo.dnd.V_TRIGGER_AUTOSCROLL, b.h / 2), rx = e.pageX - t.x, ry = e.pageY - t.y, dx = 0, dy = 0;
                        if (dojo.isSafari || dojo.isOpera) {
                            rx += dojo.body().scrollLeft, ry += dojo.body().scrollTop;
                        }
                        if (rx > 0 && rx < b.w) {
                            if (rx < w) {
                                dx = -w;
                            } else {
                                if (rx > b.w - w) {
                                    dx = w;
                                }
                            }
                        }
                        if (ry > 0 && ry < b.h) {
                            if (ry < h) {
                                dy = -h;
                            } else {
                                if (ry > b.h - h) {
                                    dy = h;
                                }
                            }
                        }
                        var _927 = n.scrollLeft, _928 = n.scrollTop;
                        n.scrollLeft = n.scrollLeft + dx;
                        n.scrollTop = n.scrollTop + dy;
                        if (_927 != n.scrollLeft || _928 != n.scrollTop) {
                            return;
                        }
                    }
                }
                try {
                    n = n.parentNode;
                } catch (x) {
                    n = null;
                }
            }
            dojo.dnd.autoScroll(e);
        };
    }
    if (!dojo._hasResource["dojo.dnd.Mover"]) {
        dojo._hasResource["dojo.dnd.Mover"] = true;
        dojo.provide("dojo.dnd.Mover");
        dojo.declare("dojo.dnd.Mover", null, {constructor: function (node, e, host) {
            this.node = dojo.byId(node);
            this.marginBox = {l: e.pageX, t: e.pageY};
            this.mouseButton = e.button;
            var h = this.host = host, d = node.ownerDocument, _92e = dojo.connect(d, "onmousemove", this, "onFirstMove");
            this.events = [dojo.connect(d, "onmousemove", this, "onMouseMove"), dojo.connect(d, "onmouseup", this, "onMouseUp"), dojo.connect(d, "ondragstart", dojo.stopEvent), dojo.connect(d.body, "onselectstart", dojo.stopEvent), _92e];
            if (h && h.onMoveStart) {
                h.onMoveStart(this);
            }
        }, onMouseMove: function (e) {
            dojo.dnd.autoScroll(e);
            var m = this.marginBox;
            this.host.onMove(this, {l: m.l + e.pageX, t: m.t + e.pageY});
            dojo.stopEvent(e);
        }, onMouseUp: function (e) {
            if (dojo.isSafari && dojo.dnd._isMac && this.mouseButton == 2 ? e.button == 0 : this.mouseButton == e.button) {
                this.destroy();
            }
            dojo.stopEvent(e);
        }, onFirstMove: function () {
            var s = this.node.style, l, t, h = this.host;
            switch (s.position) {
                case "relative":
                case "absolute":
                    l = Math.round(parseFloat(s.left));
                    t = Math.round(parseFloat(s.top));
                    break;
                default:
                    s.position = "absolute";
                    var m = dojo.marginBox(this.node);
                    var b = dojo.doc.body;
                    var bs = dojo.getComputedStyle(b);
                    var bm = dojo._getMarginBox(b, bs);
                    var bc = dojo._getContentBox(b, bs);
                    l = m.l - (bc.l - bm.l);
                    t = m.t - (bc.t - bm.t);
                    break;
            }
            this.marginBox.l = l - this.marginBox.l;
            this.marginBox.t = t - this.marginBox.t;
            if (h && h.onFirstMove) {
                h.onFirstMove(this);
            }
            dojo.disconnect(this.events.pop());
        }, destroy: function () {
            dojo.forEach(this.events, dojo.disconnect);
            var h = this.host;
            if (h && h.onMoveStop) {
                h.onMoveStop(this);
            }
            this.events = this.node = this.host = null;
        }});
    }
    if (!dojo._hasResource["dojo.dnd.Moveable"]) {
        dojo._hasResource["dojo.dnd.Moveable"] = true;
        dojo.provide("dojo.dnd.Moveable");
        dojo.declare("dojo.dnd.Moveable", null, {handle: "", delay: 0, skip: false, constructor: function (node, _93d) {
            this.node = dojo.byId(node);
            if (!_93d) {
                _93d = {};
            }
            this.handle = _93d.handle ? dojo.byId(_93d.handle) : null;
            if (!this.handle) {
                this.handle = this.node;
            }
            this.delay = _93d.delay > 0 ? _93d.delay : 0;
            this.skip = _93d.skip;
            this.mover = _93d.mover ? _93d.mover : dojo.dnd.Mover;
            this.events = [dojo.connect(this.handle, "onmousedown", this, "onMouseDown"), dojo.connect(this.handle, "ondragstart", this, "onSelectStart"), dojo.connect(this.handle, "onselectstart", this, "onSelectStart")];
        }, markupFactory: function (_93e, node) {
            return new dojo.dnd.Moveable(node, _93e);
        }, destroy: function () {
            dojo.forEach(this.events, dojo.disconnect);
            this.events = this.node = this.handle = null;
        }, onMouseDown: function (e) {
            if (this.skip && dojo.dnd.isFormElement(e)) {
                return;
            }
            if (this.delay) {
                this.events.push(dojo.connect(this.handle, "onmousemove", this, "onMouseMove"), dojo.connect(this.handle, "onmouseup", this, "onMouseUp"));
                this._lastX = e.pageX;
                this._lastY = e.pageY;
            } else {
                this.onDragDetected(e);
            }
            dojo.stopEvent(e);
        }, onMouseMove: function (e) {
            if (Math.abs(e.pageX - this._lastX) > this.delay || Math.abs(e.pageY - this._lastY) > this.delay) {
                this.onMouseUp(e);
                this.onDragDetected(e);
            }
            dojo.stopEvent(e);
        }, onMouseUp: function (e) {
            for (var i = 0; i < 2; ++i) {
                dojo.disconnect(this.events.pop());
            }
            dojo.stopEvent(e);
        }, onSelectStart: function (e) {
            if (!this.skip || !dojo.dnd.isFormElement(e)) {
                dojo.stopEvent(e);
            }
        }, onDragDetected: function (e) {
            new this.mover(this.node, e, this);
        }, onMoveStart: function (_946) {
            dojo.publish("/dnd/move/start", [_946]);
            dojo.addClass(dojo.body(), "dojoMove");
            dojo.addClass(this.node, "dojoMoveItem");
        }, onMoveStop: function (_947) {
            dojo.publish("/dnd/move/stop", [_947]);
            dojo.removeClass(dojo.body(), "dojoMove");
            dojo.removeClass(this.node, "dojoMoveItem");
        }, onFirstMove: function (_948) {
        }, onMove: function (_949, _94a) {
            this.onMoving(_949, _94a);
            var s = _949.node.style;
            s.left = _94a.l + "px";
            s.top = _94a.t + "px";
            this.onMoved(_949, _94a);
        }, onMoving: function (_94c, _94d) {
        }, onMoved: function (_94e, _94f) {
        }});
    }
    if (!dojo._hasResource["dojo.dnd.move"]) {
        dojo._hasResource["dojo.dnd.move"] = true;
        dojo.provide("dojo.dnd.move");
        dojo.declare("dojo.dnd.move.constrainedMoveable", dojo.dnd.Moveable, {constraints: function () {
        }, within: false, markupFactory: function (_950, node) {
            return new dojo.dnd.move.constrainedMoveable(node, _950);
        }, constructor: function (node, _953) {
            if (!_953) {
                _953 = {};
            }
            this.constraints = _953.constraints;
            this.within = _953.within;
        }, onFirstMove: function (_954) {
            var c = this.constraintBox = this.constraints.call(this, _954);
            c.r = c.l + c.w;
            c.b = c.t + c.h;
            if (this.within) {
                var mb = dojo.marginBox(_954.node);
                c.r -= mb.w;
                c.b -= mb.h;
            }
        }, onMove: function (_957, _958) {
            var c = this.constraintBox, s = _957.node.style;
            s.left = (_958.l < c.l ? c.l : c.r < _958.l ? c.r : _958.l) + "px";
            s.top = (_958.t < c.t ? c.t : c.b < _958.t ? c.b : _958.t) + "px";
        }});
        dojo.declare("dojo.dnd.move.boxConstrainedMoveable", dojo.dnd.move.constrainedMoveable, {box: {}, markupFactory: function (_95b, node) {
            return new dojo.dnd.move.boxConstrainedMoveable(node, _95b);
        }, constructor: function (node, _95e) {
            var box = _95e && _95e.box;
            this.constraints = function () {
                return box;
            };
        }});
        dojo.declare("dojo.dnd.move.parentConstrainedMoveable", dojo.dnd.move.constrainedMoveable, {area: "content", markupFactory: function (_960, node) {
            return new dojo.dnd.move.parentConstrainedMoveable(node, _960);
        }, constructor: function (node, _963) {
            var area = _963 && _963.area;
            this.constraints = function () {
                var n = this.node.parentNode, s = dojo.getComputedStyle(n), mb = dojo._getMarginBox(n, s);
                if (area == "margin") {
                    return mb;
                }
                var t = dojo._getMarginExtents(n, s);
                mb.l += t.l, mb.t += t.t, mb.w -= t.w, mb.h -= t.h;
                if (area == "border") {
                    return mb;
                }
                t = dojo._getBorderExtents(n, s);
                mb.l += t.l, mb.t += t.t, mb.w -= t.w, mb.h -= t.h;
                if (area == "padding") {
                    return mb;
                }
                t = dojo._getPadExtents(n, s);
                mb.l += t.l, mb.t += t.t, mb.w -= t.w, mb.h -= t.h;
                return mb;
            };
        }});
        dojo.dnd.move.constrainedMover = function (fun, _96a) {
            dojo.deprecated("dojo.dnd.move.constrainedMover, use dojo.dnd.move.constrainedMoveable instead");
            var _96b = function (node, e, _96e) {
                dojo.dnd.Mover.call(this, node, e, _96e);
            };
            dojo.extend(_96b, dojo.dnd.Mover.prototype);
            dojo.extend(_96b, {onMouseMove: function (e) {
                dojo.dnd.autoScroll(e);
                var m = this.marginBox, c = this.constraintBox, l = m.l + e.pageX, t = m.t + e.pageY;
                l = l < c.l ? c.l : c.r < l ? c.r : l;
                t = t < c.t ? c.t : c.b < t ? c.b : t;
                this.host.onMove(this, {l: l, t: t});
            }, onFirstMove: function () {
                dojo.dnd.Mover.prototype.onFirstMove.call(this);
                var c = this.constraintBox = fun.call(this);
                c.r = c.l + c.w;
                c.b = c.t + c.h;
                if (_96a) {
                    var mb = dojo.marginBox(this.node);
                    c.r -= mb.w;
                    c.b -= mb.h;
                }
            }});
            return _96b;
        };
        dojo.dnd.move.boxConstrainedMover = function (box, _977) {
            dojo.deprecated("dojo.dnd.move.boxConstrainedMover, use dojo.dnd.move.boxConstrainedMoveable instead");
            return dojo.dnd.move.constrainedMover(function () {
                return box;
            }, _977);
        };
        dojo.dnd.move.parentConstrainedMover = function (area, _979) {
            dojo.deprecated("dojo.dnd.move.parentConstrainedMover, use dojo.dnd.move.parentConstrainedMoveable instead");
            var fun = function () {
                var n = this.node.parentNode, s = dojo.getComputedStyle(n), mb = dojo._getMarginBox(n, s);
                if (area == "margin") {
                    return mb;
                }
                var t = dojo._getMarginExtents(n, s);
                mb.l += t.l, mb.t += t.t, mb.w -= t.w, mb.h -= t.h;
                if (area == "border") {
                    return mb;
                }
                t = dojo._getBorderExtents(n, s);
                mb.l += t.l, mb.t += t.t, mb.w -= t.w, mb.h -= t.h;
                if (area == "padding") {
                    return mb;
                }
                t = dojo._getPadExtents(n, s);
                mb.l += t.l, mb.t += t.t, mb.w -= t.w, mb.h -= t.h;
                return mb;
            };
            return dojo.dnd.move.constrainedMover(fun, _979);
        };
        dojo.dnd.constrainedMover = dojo.dnd.move.constrainedMover;
        dojo.dnd.boxConstrainedMover = dojo.dnd.move.boxConstrainedMover;
        dojo.dnd.parentConstrainedMover = dojo.dnd.move.parentConstrainedMover;
    }
    if (!dojo._hasResource["dojo.dnd.TimedMoveable"]) {
        dojo._hasResource["dojo.dnd.TimedMoveable"] = true;
        dojo.provide("dojo.dnd.TimedMoveable");
        (function () {
            var _97f = dojo.dnd.Moveable.prototype.onMove;
            dojo.declare("dojo.dnd.TimedMoveable", dojo.dnd.Moveable, {timeout: 40, constructor: function (node, _981) {
                if (!_981) {
                    _981 = {};
                }
                if (_981.timeout && typeof _981.timeout == "number" && _981.timeout >= 0) {
                    this.timeout = _981.timeout;
                }
            }, markupFactory: function (_982, node) {
                return new dojo.dnd.TimedMoveable(node, _982);
            }, onMoveStop: function (_984) {
                if (_984._timer) {
                    clearTimeout(_984._timer);
                    _97f.call(this, _984, _984._leftTop);
                }
                dojo.dnd.Moveable.prototype.onMoveStop.apply(this, arguments);
            }, onMove: function (_985, _986) {
                _985._leftTop = _986;
                if (!_985._timer) {
                    var _t = this;
                    _985._timer = setTimeout(function () {
                        _985._timer = null;
                        _97f.call(_t, _985, _985._leftTop);
                    }, this.timeout);
                }
            }});
        })();
    }
    if (!dojo._hasResource["dojo.fx"]) {
        dojo._hasResource["dojo.fx"] = true;
        dojo.provide("dojo.fx");
        dojo.provide("dojo.fx.Toggler");
        (function () {
            var _988 = {_fire: function (evt, args) {
                if (this[evt]) {
                    this[evt].apply(this, args || []);
                }
                return this;
            }};
            var _98b = function (_98c) {
                this._index = -1;
                this._animations = _98c || [];
                this._current = this._onAnimateCtx = this._onEndCtx = null;
                this.duration = 0;
                dojo.forEach(this._animations, function (a) {
                    this.duration += a.duration;
                    if (a.delay) {
                        this.duration += a.delay;
                    }
                }, this);
            };
            dojo.extend(_98b, {_onAnimate: function () {
                this._fire("onAnimate", arguments);
            }, _onEnd: function () {
                dojo.disconnect(this._onAnimateCtx);
                dojo.disconnect(this._onEndCtx);
                this._onAnimateCtx = this._onEndCtx = null;
                if (this._index + 1 == this._animations.length) {
                    this._fire("onEnd");
                } else {
                    this._current = this._animations[++this._index];
                    this._onAnimateCtx = dojo.connect(this._current, "onAnimate", this, "_onAnimate");
                    this._onEndCtx = dojo.connect(this._current, "onEnd", this, "_onEnd");
                    this._current.play(0, true);
                }
            }, play: function (_98e, _98f) {
                if (!this._current) {
                    this._current = this._animations[this._index = 0];
                }
                if (!_98f && this._current.status() == "playing") {
                    return this;
                }
                var _990 = dojo.connect(this._current, "beforeBegin", this, function () {
                    this._fire("beforeBegin");
                }), _991 = dojo.connect(this._current, "onBegin", this, function (arg) {
                    this._fire("onBegin", arguments);
                }), _993 = dojo.connect(this._current, "onPlay", this, function (arg) {
                    this._fire("onPlay", arguments);
                    dojo.disconnect(_990);
                    dojo.disconnect(_991);
                    dojo.disconnect(_993);
                });
                if (this._onAnimateCtx) {
                    dojo.disconnect(this._onAnimateCtx);
                }
                this._onAnimateCtx = dojo.connect(this._current, "onAnimate", this, "_onAnimate");
                if (this._onEndCtx) {
                    dojo.disconnect(this._onEndCtx);
                }
                this._onEndCtx = dojo.connect(this._current, "onEnd", this, "_onEnd");
                this._current.play.apply(this._current, arguments);
                return this;
            }, pause: function () {
                if (this._current) {
                    var e = dojo.connect(this._current, "onPause", this, function (arg) {
                        this._fire("onPause", arguments);
                        dojo.disconnect(e);
                    });
                    this._current.pause();
                }
                return this;
            }, gotoPercent: function (_997, _998) {
                this.pause();
                var _999 = this.duration * _997;
                this._current = null;
                dojo.some(this._animations, function (a) {
                    if (a.duration <= _999) {
                        this._current = a;
                        return true;
                    }
                    _999 -= a.duration;
                    return false;
                });
                if (this._current) {
                    this._current.gotoPercent(_999 / this._current.duration, _998);
                }
                return this;
            }, stop: function (_99b) {
                if (this._current) {
                    if (_99b) {
                        for (; this._index + 1 < this._animations.length; ++this._index) {
                            this._animations[this._index].stop(true);
                        }
                        this._current = this._animations[this._index];
                    }
                    var e = dojo.connect(this._current, "onStop", this, function (arg) {
                        this._fire("onStop", arguments);
                        dojo.disconnect(e);
                    });
                    this._current.stop();
                }
                return this;
            }, status: function () {
                return this._current ? this._current.status() : "stopped";
            }, destroy: function () {
                if (this._onAnimateCtx) {
                    dojo.disconnect(this._onAnimateCtx);
                }
                if (this._onEndCtx) {
                    dojo.disconnect(this._onEndCtx);
                }
            }});
            dojo.extend(_98b, _988);
            dojo.fx.chain = function (_99e) {
                return new _98b(_99e);
            };
            var _99f = function (_9a0) {
                this._animations = _9a0 || [];
                this._connects = [];
                this._finished = 0;
                this.duration = 0;
                dojo.forEach(_9a0, function (a) {
                    var _9a2 = a.duration;
                    if (a.delay) {
                        _9a2 += a.delay;
                    }
                    if (this.duration < _9a2) {
                        this.duration = _9a2;
                    }
                    this._connects.push(dojo.connect(a, "onEnd", this, "_onEnd"));
                }, this);
                this._pseudoAnimation = new dojo._Animation({curve: [0, 1], duration: this.duration});
                dojo.forEach(["beforeBegin", "onBegin", "onPlay", "onAnimate", "onPause", "onStop"], function (evt) {
                    this._connects.push(dojo.connect(this._pseudoAnimation, evt, dojo.hitch(this, "_fire", evt)));
                }, this);
            };
            dojo.extend(_99f, {_doAction: function (_9a4, args) {
                dojo.forEach(this._animations, function (a) {
                    a[_9a4].apply(a, args);
                });
                return this;
            }, _onEnd: function () {
                if (++this._finished == this._animations.length) {
                    this._fire("onEnd");
                }
            }, _call: function (_9a7, args) {
                var t = this._pseudoAnimation;
                t[_9a7].apply(t, args);
            }, play: function (_9aa, _9ab) {
                this._finished = 0;
                this._doAction("play", arguments);
                this._call("play", arguments);
                return this;
            }, pause: function () {
                this._doAction("pause", arguments);
                this._call("pause", arguments);
                return this;
            }, gotoPercent: function (_9ac, _9ad) {
                var ms = this.duration * _9ac;
                dojo.forEach(this._animations, function (a) {
                    a.gotoPercent(a.duration < ms ? 1 : (ms / a.duration), _9ad);
                });
                this._call("gotoPercent", arguments);
                return this;
            }, stop: function (_9b0) {
                this._doAction("stop", arguments);
                this._call("stop", arguments);
                return this;
            }, status: function () {
                return this._pseudoAnimation.status();
            }, destroy: function () {
                dojo.forEach(this._connects, dojo.disconnect);
            }});
            dojo.extend(_99f, _988);
            dojo.fx.combine = function (_9b1) {
                return new _99f(_9b1);
            };
        })();
        dojo.declare("dojo.fx.Toggler", null, {constructor: function (args) {
            var _t = this;
            dojo.mixin(_t, args);
            _t.node = args.node;
            _t._showArgs = dojo.mixin({}, args);
            _t._showArgs.node = _t.node;
            _t._showArgs.duration = _t.showDuration;
            _t.showAnim = _t.showFunc(_t._showArgs);
            _t._hideArgs = dojo.mixin({}, args);
            _t._hideArgs.node = _t.node;
            _t._hideArgs.duration = _t.hideDuration;
            _t.hideAnim = _t.hideFunc(_t._hideArgs);
            dojo.connect(_t.showAnim, "beforeBegin", dojo.hitch(_t.hideAnim, "stop", true));
            dojo.connect(_t.hideAnim, "beforeBegin", dojo.hitch(_t.showAnim, "stop", true));
        }, node: null, showFunc: dojo.fadeIn, hideFunc: dojo.fadeOut, showDuration: 200, hideDuration: 200, show: function (_9b4) {
            return this.showAnim.play(_9b4 || 0);
        }, hide: function (_9b5) {
            return this.hideAnim.play(_9b5 || 0);
        }});
        dojo.fx.wipeIn = function (args) {
            args.node = dojo.byId(args.node);
            var node = args.node, s = node.style, o;
            var anim = dojo.animateProperty(dojo.mixin({properties: {height: {start: function () {
                o = s.overflow;
                s.overflow = "hidden";
                if (s.visibility == "hidden" || s.display == "none") {
                    s.height = "1px";
                    s.display = "";
                    s.visibility = "";
                    return 1;
                } else {
                    var _9bb = dojo.style(node, "height");
                    return Math.max(_9bb, 1);
                }
            }, end: function () {
                return node.scrollHeight;
            }}}}, args));
            dojo.connect(anim, "onEnd", function () {
                s.height = "auto";
                s.overflow = o;
            });
            return anim;
        };
        dojo.fx.wipeOut = function (args) {
            var node = args.node = dojo.byId(args.node);
            var s = node.style;
            var o;
            var anim = dojo.animateProperty(dojo.mixin({properties: {height: {end: 1}}}, args));
            dojo.connect(anim, "beforeBegin", function () {
                o = s.overflow;
                s.overflow = "hidden";
                s.display = "";
            });
            dojo.connect(anim, "onEnd", function () {
                s.overflow = o;
                s.height = "auto";
                s.display = "none";
            });
            return anim;
        };
        dojo.fx.slideTo = function (args) {
            var node = (args.node = dojo.byId(args.node));
            var top = null;
            var left = null;
            var init = (function (n) {
                return function () {
                    var cs = dojo.getComputedStyle(n);
                    var pos = cs.position;
                    top = (pos == "absolute" ? n.offsetTop : parseInt(cs.top) || 0);
                    left = (pos == "absolute" ? n.offsetLeft : parseInt(cs.left) || 0);
                    if (pos != "absolute" && pos != "relative") {
                        var ret = dojo.coords(n, true);
                        top = ret.y;
                        left = ret.x;
                        n.style.position = "absolute";
                        n.style.top = top + "px";
                        n.style.left = left + "px";
                    }
                };
            })(node);
            init();
            var anim = dojo.animateProperty(dojo.mixin({properties: {top: {end: args.top || 0}, left: {end: args.left || 0}}}, args));
            dojo.connect(anim, "beforeBegin", anim, init);
            return anim;
        };
    }
    if (!dojo._hasResource["dijit._Container"]) {
        dojo._hasResource["dijit._Container"] = true;
        dojo.provide("dijit._Container");
        dojo.declare("dijit._Contained", null, {getParent: function () {
            for (var p = this.domNode.parentNode; p; p = p.parentNode) {
                var id = p.getAttribute && p.getAttribute("widgetId");
                if (id) {
                    var _9cd = dijit.byId(id);
                    return _9cd.isContainer ? _9cd : null;
                }
            }
            return null;
        }, _getSibling: function (_9ce) {
            var node = this.domNode;
            do {
                node = node[_9ce + "Sibling"];
            } while (node && node.nodeType != 1);
            if (!node) {
                return null;
            }
            var id = node.getAttribute("widgetId");
            return dijit.byId(id);
        }, getPreviousSibling: function () {
            return this._getSibling("previous");
        }, getNextSibling: function () {
            return this._getSibling("next");
        }, getIndexInParent: function () {
            var p = this.getParent();
            if (!p || !p.getIndexOfChild) {
                return -1;
            }
            return p.getIndexOfChild(this);
        }});
        dojo.declare("dijit._Container", null, {isContainer: true, buildRendering: function () {
            this.inherited(arguments);
            if (!this.containerNode) {
                this.containerNode = this.domNode;
            }
        }, addChild: function (_9d2, _9d3) {
            var _9d4 = this.containerNode;
            if (_9d3 && typeof _9d3 == "number") {
                var _9d5 = dojo.query("> [widgetId]", _9d4);
                if (_9d5 && _9d5.length >= _9d3) {
                    _9d4 = _9d5[_9d3 - 1];
                    _9d3 = "after";
                }
            }
            dojo.place(_9d2.domNode, _9d4, _9d3);
            if (this._started && !_9d2._started) {
                _9d2.startup();
            }
        }, removeChild: function (_9d6) {
            if (typeof _9d6 == "number" && _9d6 > 0) {
                _9d6 = this.getChildren()[_9d6];
            }
            if (!_9d6 || !_9d6.domNode) {
                return;
            }
            var node = _9d6.domNode;
            node.parentNode.removeChild(node);
        }, _nextElement: function (node) {
            do {
                node = node.nextSibling;
            } while (node && node.nodeType != 1);
            return node;
        }, _firstElement: function (node) {
            node = node.firstChild;
            if (node && node.nodeType != 1) {
                node = this._nextElement(node);
            }
            return node;
        }, getChildren: function () {
            return dojo.query("> [widgetId]", this.containerNode).map(dijit.byNode);
        }, hasChildren: function () {
            return !!this._firstElement(this.containerNode);
        }, destroyDescendants: function (_9da) {
            dojo.forEach(this.getChildren(), function (_9db) {
                _9db.destroyRecursive(_9da);
            });
        }, _getSiblingOfChild: function (_9dc, dir) {
            var node = _9dc.domNode;
            var _9df = (dir > 0 ? "nextSibling" : "previousSibling");
            do {
                node = node[_9df];
            } while (node && (node.nodeType != 1 || !dijit.byNode(node)));
            return node ? dijit.byNode(node) : null;
        }, getIndexOfChild: function (_9e0) {
            var _9e1 = this.getChildren();
            for (var i = 0, c; c = _9e1[i]; i++) {
                if (c == _9e0) {
                    return i;
                }
            }
            return -1;
        }});
        dojo.declare("dijit._KeyNavContainer", [dijit._Container], {_keyNavCodes: {}, connectKeyNavHandlers: function (_9e4, _9e5) {
            var _9e6 = this._keyNavCodes = {};
            var prev = dojo.hitch(this, this.focusPrev);
            var next = dojo.hitch(this, this.focusNext);
            dojo.forEach(_9e4, function (code) {
                _9e6[code] = prev;
            });
            dojo.forEach(_9e5, function (code) {
                _9e6[code] = next;
            });
            this.connect(this.domNode, "onkeypress", "_onContainerKeypress");
            this.connect(this.domNode, "onfocus", "_onContainerFocus");
        }, startupKeyNavChildren: function () {
            dojo.forEach(this.getChildren(), dojo.hitch(this, "_startupChild"));
        }, addChild: function (_9eb, _9ec) {
            dijit._KeyNavContainer.superclass.addChild.apply(this, arguments);
            this._startupChild(_9eb);
        }, focus: function () {
            this.focusFirstChild();
        }, focusFirstChild: function () {
            this.focusChild(this._getFirstFocusableChild());
        }, focusNext: function () {
            if (this.focusedChild && this.focusedChild.hasNextFocalNode && this.focusedChild.hasNextFocalNode()) {
                this.focusedChild.focusNext();
                return;
            }
            var _9ed = this._getNextFocusableChild(this.focusedChild, 1);
            if (_9ed.getFocalNodes) {
                this.focusChild(_9ed, _9ed.getFocalNodes()[0]);
            } else {
                this.focusChild(_9ed);
            }
        }, focusPrev: function () {
            if (this.focusedChild && this.focusedChild.hasPrevFocalNode && this.focusedChild.hasPrevFocalNode()) {
                this.focusedChild.focusPrev();
                return;
            }
            var _9ee = this._getNextFocusableChild(this.focusedChild, -1);
            if (_9ee.getFocalNodes) {
                var _9ef = _9ee.getFocalNodes();
                this.focusChild(_9ee, _9ef[_9ef.length - 1]);
            } else {
                this.focusChild(_9ee);
            }
        }, focusChild: function (_9f0, node) {
            if (_9f0) {
                if (this.focusedChild && _9f0 !== this.focusedChild) {
                    this._onChildBlur(this.focusedChild);
                }
                this.focusedChild = _9f0;
                if (node && _9f0.focusFocalNode) {
                    _9f0.focusFocalNode(node);
                } else {
                    _9f0.focus();
                }
            }
        }, _startupChild: function (_9f2) {
            if (_9f2.getFocalNodes) {
                dojo.forEach(_9f2.getFocalNodes(), function (node) {
                    dojo.attr(node, "tabindex", -1);
                    this._connectNode(node);
                }, this);
            } else {
                var node = _9f2.focusNode || _9f2.domNode;
                if (_9f2.isFocusable()) {
                    dojo.attr(node, "tabindex", -1);
                }
                this._connectNode(node);
            }
        }, _connectNode: function (node) {
            this.connect(node, "onfocus", "_onNodeFocus");
            this.connect(node, "onblur", "_onNodeBlur");
        }, _onContainerFocus: function (evt) {
            if (evt.target === this.domNode) {
                this.focusFirstChild();
            }
        }, _onContainerKeypress: function (evt) {
            if (evt.ctrlKey || evt.altKey) {
                return;
            }
            var func = this._keyNavCodes[evt.charOrCode];
            if (func) {
                func();
                dojo.stopEvent(evt);
            }
        }, _onNodeFocus: function (evt) {
            dojo.attr(this.domNode, "tabindex", -1);
            var _9fa = dijit.getEnclosingWidget(evt.target);
            if (_9fa && _9fa.isFocusable()) {
                this.focusedChild = _9fa;
            }
            dojo.stopEvent(evt);
        }, _onNodeBlur: function (evt) {
            if (this.tabIndex) {
                dojo.attr(this.domNode, "tabindex", this.tabIndex);
            }
            dojo.stopEvent(evt);
        }, _onChildBlur: function (_9fc) {
        }, _getFirstFocusableChild: function () {
            return this._getNextFocusableChild(null, 1);
        }, _getNextFocusableChild: function (_9fd, dir) {
            if (_9fd) {
                _9fd = this._getSiblingOfChild(_9fd, dir);
            }
            var _9ff = this.getChildren();
            for (var i = 0; i < _9ff.length; i++) {
                if (!_9fd) {
                    _9fd = _9ff[(dir > 0) ? 0 : (_9ff.length - 1)];
                }
                if (_9fd.isFocusable()) {
                    return _9fd;
                }
                _9fd = this._getSiblingOfChild(_9fd, dir);
            }
            return null;
        }});
    }
    if (!dojo._hasResource["dijit.layout._LayoutWidget"]) {
        dojo._hasResource["dijit.layout._LayoutWidget"] = true;
        dojo.provide("dijit.layout._LayoutWidget");
        dojo.declare("dijit.layout._LayoutWidget", [dijit._Widget, dijit._Container, dijit._Contained], {baseClass: "dijitLayoutContainer", isLayoutContainer: true, postCreate: function () {
            dojo.addClass(this.domNode, "dijitContainer");
            dojo.addClass(this.domNode, this.baseClass);
        }, startup: function () {
            if (this._started) {
                return;
            }
            dojo.forEach(this.getChildren(), function (_a01) {
                _a01.startup();
            });
            if (!this.getParent || !this.getParent()) {
                this.resize();
                this.connect(dojo.global, "onresize", "resize");
            }
            this.inherited(arguments);
        }, resize: function (_a02, _a03) {
            var node = this.domNode;
            if (_a02) {
                dojo.marginBox(node, _a02);
                if (_a02.t) {
                    node.style.top = _a02.t + "px";
                }
                if (_a02.l) {
                    node.style.left = _a02.l + "px";
                }
            }
            var mb = _a03 || {};
            dojo.mixin(mb, _a02 || {});
            if (!("h" in mb) || !("w" in mb)) {
                mb = dojo.mixin(dojo.marginBox(node), mb);
            }
            var cs = dojo.getComputedStyle(node);
            var me = dojo._getMarginExtents(node, cs);
            var be = dojo._getBorderExtents(node, cs);
            var bb = this._borderBox = {w: mb.w - (me.w + be.w), h: mb.h - (me.h + be.h)};
            var pe = dojo._getPadExtents(node, cs);
            this._contentBox = {l: dojo._toPixelValue(node, cs.paddingLeft), t: dojo._toPixelValue(node, cs.paddingTop), w: bb.w - pe.w, h: bb.h - pe.h};
            this.layout();
        }, layout: function () {
        }, _setupChild: function (_a0b) {
            if (_a0b.baseClass) {
                dojo.addClass(_a0b.domNode, this.baseClass + "-" + _a0b.baseClass);
            }
        }, addChild: function (_a0c, _a0d) {
            this.inherited(arguments);
            if (this._started) {
                this._setupChild(_a0c);
            }
        }, removeChild: function (_a0e) {
            if (_a0e.baseClass) {
                dojo.removeClass(_a0e.domNode, this.baseClass + "-" + _a0e.baseClass);
            }
            this.inherited(arguments);
        }});
        dijit.layout.marginBox2contentBox = function (node, mb) {
            var cs = dojo.getComputedStyle(node);
            var me = dojo._getMarginExtents(node, cs);
            var pb = dojo._getPadBorderExtents(node, cs);
            return {l: dojo._toPixelValue(node, cs.paddingLeft), t: dojo._toPixelValue(node, cs.paddingTop), w: mb.w - (me.w + pb.w), h: mb.h - (me.h + pb.h)};
        };
        (function () {
            var _a14 = function (word) {
                return word.substring(0, 1).toUpperCase() + word.substring(1);
            };
            var size = function (_a17, dim) {
                _a17.resize ? _a17.resize(dim) : dojo.marginBox(_a17.domNode, dim);
                dojo.mixin(_a17, dojo.marginBox(_a17.domNode));
                dojo.mixin(_a17, dim);
            };
            dijit.layout.layoutChildren = function (_a19, dim, _a1b) {
                dim = dojo.mixin({}, dim);
                dojo.addClass(_a19, "dijitLayoutContainer");
                _a1b = dojo.filter(_a1b,function (item) {
                    return item.layoutAlign != "client";
                }).concat(dojo.filter(_a1b, function (item) {
                    return item.layoutAlign == "client";
                }));
                dojo.forEach(_a1b, function (_a1e) {
                    var elm = _a1e.domNode, pos = _a1e.layoutAlign;
                    var _a21 = elm.style;
                    _a21.left = dim.l + "px";
                    _a21.top = dim.t + "px";
                    _a21.bottom = _a21.right = "auto";
                    dojo.addClass(elm, "dijitAlign" + _a14(pos));
                    if (pos == "top" || pos == "bottom") {
                        size(_a1e, {w: dim.w});
                        dim.h -= _a1e.h;
                        if (pos == "top") {
                            dim.t += _a1e.h;
                        } else {
                            _a21.top = dim.t + dim.h + "px";
                        }
                    } else {
                        if (pos == "left" || pos == "right") {
                            size(_a1e, {h: dim.h});
                            dim.w -= _a1e.w;
                            if (pos == "left") {
                                dim.l += _a1e.w;
                            } else {
                                _a21.left = dim.l + dim.w + "px";
                            }
                        } else {
                            if (pos == "client") {
                                size(_a1e, dim);
                            }
                        }
                    }
                });
            };
        })();
    }
    if (!dojo._hasResource["dojo.html"]) {
        dojo._hasResource["dojo.html"] = true;
        dojo.provide("dojo.html");
        (function () {
            var _a22 = 0;
            dojo.html._secureForInnerHtml = function (cont) {
                return cont.replace(/(?:\s*<!DOCTYPE\s[^>]+>|<title[^>]*>[\s\S]*?<\/title>)/ig, "");
            };
            dojo.html._emptyNode = function (node) {
                while (node.firstChild) {
                    dojo._destroyElement(node.firstChild);
                }
            };
            dojo.html._setNodeContent = function (node, cont, _a27) {
                if (_a27) {
                    dojo.html._emptyNode(node);
                }
                if (typeof cont == "string") {
                    var pre = "", post = "", walk = 0, name = node.nodeName.toLowerCase();
                    switch (name) {
                        case "tr":
                            pre = "<tr>";
                            post = "</tr>";
                            walk += 1;
                        case "tbody":
                        case "thead":
                            pre = "<tbody>" + pre;
                            post += "</tbody>";
                            walk += 1;
                        case "table":
                            pre = "<table>" + pre;
                            post += "</table>";
                            walk += 1;
                            break;
                    }
                    if (walk) {
                        var n = node.ownerDocument.createElement("div");
                        n.innerHTML = pre + cont + post;
                        do {
                            n = n.firstChild;
                        } while (--walk);
                        dojo.forEach(n.childNodes, function (n) {
                            node.appendChild(n.cloneNode(true));
                        });
                    } else {
                        node.innerHTML = cont;
                    }
                } else {
                    if (cont.nodeType) {
                        node.appendChild(cont);
                    } else {
                        dojo.forEach(cont, function (n) {
                            node.appendChild(n.cloneNode(true));
                        });
                    }
                }
                return node;
            };
            dojo.declare("dojo.html._ContentSetter", null, {node: "", content: "", id: "", cleanContent: false, extractContent: false, parseContent: false, constructor: function (_a2f, node) {
                dojo.mixin(this, _a2f || {});
                node = this.node = dojo.byId(this.node || node);
                if (!this.id) {
                    this.id = ["Setter", (node) ? node.id || node.tagName : "", _a22++].join("_");
                }
                if (!(this.node || node)) {
                    new Error(this.declaredClass + ": no node provided to " + this.id);
                }
            }, set: function (cont, _a32) {
                if (undefined !== cont) {
                    this.content = cont;
                }
                if (_a32) {
                    this._mixin(_a32);
                }
                this.onBegin();
                this.setContent();
                this.onEnd();
                return this.node;
            }, setContent: function () {
                var node = this.node;
                if (!node) {
                    console.error("setContent given no node");
                }
                try {
                    node = dojo.html._setNodeContent(node, this.content);
                } catch (e) {
                    var _a34 = this.onContentError(e);
                    try {
                        node.innerHTML = _a34;
                    } catch (e) {
                        console.error("Fatal " + this.declaredClass + ".setContent could not change content due to " + e.message, e);
                    }
                }
                this.node = node;
            }, empty: function () {
                if (this.parseResults && this.parseResults.length) {
                    dojo.forEach(this.parseResults, function (w) {
                        if (w.destroy) {
                            w.destroy();
                        }
                    });
                    delete this.parseResults;
                }
                dojo.html._emptyNode(this.node);
            }, onBegin: function () {
                var cont = this.content;
                if (dojo.isString(cont)) {
                    if (this.cleanContent) {
                        cont = dojo.html._secureForInnerHtml(cont);
                    }
                    if (this.extractContent) {
                        var _a37 = cont.match(/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im);
                        if (_a37) {
                            cont = _a37[1];
                        }
                    }
                }
                this.empty();
                this.content = cont;
                return this.node;
            }, onEnd: function () {
                if (this.parseContent) {
                    this._parse();
                }
                return this.node;
            }, tearDown: function () {
                delete this.parseResults;
                delete this.node;
                delete this.content;
            }, onContentError: function (err) {
                return "Error occured setting content: " + err;
            }, _mixin: function (_a39) {
                var _a3a = {}, key;
                for (key in _a39) {
                    if (key in _a3a) {
                        continue;
                    }
                    this[key] = _a39[key];
                }
            }, _parse: function () {
                var _a3c = this.node;
                try {
                    this.parseResults = dojo.parser.parse(_a3c, true);
                } catch (e) {
                    this._onError("Content", e, "Error parsing in _ContentSetter#" + this.id);
                }
            }, _onError: function (type, err, _a3f) {
                var _a40 = this["on" + type + "Error"].call(this, err);
                if (_a3f) {
                    console.error(_a3f, err);
                } else {
                    if (_a40) {
                        dojo.html._setNodeContent(this.node, _a40, true);
                    }
                }
            }});
            dojo.html.set = function (node, cont, _a43) {
                if (undefined == cont) {
                    console.warn("dojo.html.set: no cont argument provided, using empty string");
                    cont = "";
                }
                if (!_a43) {
                    return dojo.html._setNodeContent(node, cont, true);
                } else {
                    var op = new dojo.html._ContentSetter(dojo.mixin(_a43, {content: cont, node: node}));
                    return op.set();
                }
            };
        })();
    }
    if (!dojo._hasResource["dijit.layout.ContentPane"]) {
        dojo._hasResource["dijit.layout.ContentPane"] = true;
        dojo.provide("dijit.layout.ContentPane");
        dojo.declare("dijit.layout.ContentPane", dijit._Widget, {href: "", extractContent: false, parseOnLoad: true, preventCache: false, preload: false, refreshOnShow: false, loadingMessage: "<span class='dijitContentPaneLoading'>${loadingState}</span>", errorMessage: "<span class='dijitContentPaneError'>${errorState}</span>", isLoaded: false, baseClass: "dijitContentPane", doLayout: true, _isRealContent: true, postMixInProperties: function () {
            this.inherited(arguments);
            var _a45 = dojo.i18n.getLocalization("dijit", "loading", this.lang);
            this.loadingMessage = dojo.string.substitute(this.loadingMessage, _a45);
            this.errorMessage = dojo.string.substitute(this.errorMessage, _a45);
        }, buildRendering: function () {
            this.inherited(arguments);
            if (!this.containerNode) {
                this.containerNode = this.domNode;
            }
        }, postCreate: function () {
            this.domNode.title = "";
            if (!dijit.hasWaiRole(this.domNode)) {
                dijit.setWaiRole(this.domNode, "group");
            }
            dojo.addClass(this.domNode, this.baseClass);
        }, startup: function () {
            if (this._started) {
                return;
            }
            if (this.doLayout != "false" && this.doLayout !== false) {
                this._checkIfSingleChild();
                if (this._singleChild) {
                    this._singleChild.startup();
                }
            }
            this._loadCheck();
            this.inherited(arguments);
        }, _checkIfSingleChild: function () {
            var _a46 = dojo.query(">", this.containerNode), _a47 = _a46.filter(function (node) {
                return dojo.hasAttr(node, "dojoType") || dojo.hasAttr(node, "widgetId");
            }), _a49 = dojo.filter(_a47.map(dijit.byNode), function (_a4a) {
                return _a4a && _a4a.domNode && _a4a.resize;
            });
            if (_a46.length == _a47.length && _a49.length == 1) {
                this.isContainer = true;
                this._singleChild = _a49[0];
            } else {
                delete this.isContainer;
                delete this._singleChild;
            }
        }, refresh: function () {
            return this._prepareLoad(true);
        }, setHref: function (href) {
            dojo.deprecated("dijit.layout.ContentPane.setHref() is deprecated.\tUse attr('href', ...) instead.", "", "2.0");
            return this.attr("href", href);
        }, _setHrefAttr: function (href) {
            this.href = href;
            if (this._created) {
                return this._prepareLoad();
            }
        }, setContent: function (data) {
            dojo.deprecated("dijit.layout.ContentPane.setContent() is deprecated.  Use attr('content', ...) instead.", "", "2.0");
            this.attr("content", data);
        }, _setContentAttr: function (data) {
            this.href = "";
            this.cancel();
            this._setContent(data || "");
            this._isDownloaded = false;
        }, _getContentAttr: function () {
            return this.containerNode.innerHTML;
        }, cancel: function () {
            if (this._xhrDfd && (this._xhrDfd.fired == -1)) {
                this._xhrDfd.cancel();
            }
            delete this._xhrDfd;
        }, destroyRecursive: function (_a4f) {
            if (this._beingDestroyed) {
                return;
            }
            this._beingDestroyed = true;
            this.inherited(arguments);
        }, resize: function (size) {
            dojo.marginBox(this.domNode, size);
            var node = this.containerNode, mb = dojo.mixin(dojo.marginBox(node), size || {});
            var cb = this._contentBox = dijit.layout.marginBox2contentBox(node, mb);
            if (this._singleChild && this._singleChild.resize) {
                this._singleChild.resize({w: cb.w, h: cb.h});
            }
        }, _prepareLoad: function (_a54) {
            this.cancel();
            this.isLoaded = false;
            this._loadCheck(_a54);
        }, _isShown: function () {
            if ("open" in this) {
                return this.open;
            } else {
                var node = this.domNode;
                return (node.style.display != "none") && (node.style.visibility != "hidden");
            }
        }, _loadCheck: function (_a56) {
            var _a57 = this._isShown();
            if (this.href && (_a56 || (this.preload && !this.isLoaded && !this._xhrDfd) || (this.refreshOnShow && _a57 && !this._xhrDfd) || (!this.isLoaded && _a57 && !this._xhrDfd))) {
                this._downloadExternalContent();
            }
        }, _downloadExternalContent: function () {
            this._setContent(this.onDownloadStart(), true);
            var self = this;
            var _a59 = {preventCache: (this.preventCache || this.refreshOnShow), url: this.href, handleAs: "text"};
            if (dojo.isObject(this.ioArgs)) {
                dojo.mixin(_a59, this.ioArgs);
            }
            var hand = this._xhrDfd = (this.ioMethod || dojo.xhrGet)(_a59);
            hand.addCallback(function (html) {
                try {
                    self._isDownloaded = true;
                    self._setContent(html, false);
                    self.onDownloadEnd();
                } catch (err) {
                    self._onError("Content", err);
                }
                delete self._xhrDfd;
                return html;
            });
            hand.addErrback(function (err) {
                if (!hand.cancelled) {
                    self._onError("Download", err);
                }
                delete self._xhrDfd;
                return err;
            });
        }, _onLoadHandler: function (data) {
            this.isLoaded = true;
            try {
                this.onLoad(data);
            } catch (e) {
                console.error("Error " + this.widgetId + " running custom onLoad code");
            }
        }, _onUnloadHandler: function () {
            this.isLoaded = false;
            try {
                this.onUnload();
            } catch (e) {
                console.error("Error " + this.widgetId + " running custom onUnload code");
            }
        }, destroyDescendants: function () {
            if (this._isRealContent) {
                this._onUnloadHandler();
            }
            var _a5e = this._contentSetter;
            if (_a5e) {
                _a5e.empty();
            } else {
                this.inherited(arguments);
                dojo.html._emptyNode(this.containerNode);
            }
        }, _setContent: function (cont, _a60) {
            this.destroyDescendants();
            this._isRealContent = !_a60;
            var _a61 = this._contentSetter;
            if (!(_a61 && _a61 instanceof dojo.html._ContentSetter)) {
                _a61 = this._contentSetter = new dojo.html._ContentSetter({node: this.containerNode, _onError: dojo.hitch(this, this._onError), onContentError: dojo.hitch(this, function (e) {
                    var _a63 = this.onContentError(e);
                    try {
                        this.containerNode.innerHTML = _a63;
                    } catch (e) {
                        console.error("Fatal " + this.id + " could not change content due to " + e.message, e);
                    }
                })});
            }
            var _a64 = dojo.mixin({cleanContent: this.cleanContent, extractContent: this.extractContent, parseContent: this.parseOnLoad}, this._contentSetterParams || {});
            dojo.mixin(_a61, _a64);
            _a61.set((dojo.isObject(cont) && cont.domNode) ? cont.domNode : cont);
            delete this._contentSetterParams;
            if (!_a60) {
                if (this.doLayout != "false" && this.doLayout !== false) {
                    this._checkIfSingleChild();
                    if (this._singleChild && this._singleChild.resize) {
                        this._singleChild.startup();
                        var cb = this._contentBox || dojo.contentBox(this.containerNode);
                        this._singleChild.resize({w: cb.w, h: cb.h});
                    }
                }
                this._onLoadHandler(cont);
            }
        }, _onError: function (type, err, _a68) {
            var _a69 = this["on" + type + "Error"].call(this, err);
            if (_a68) {
                console.error(_a68, err);
            } else {
                if (_a69) {
                    this._setContent(_a69, true);
                }
            }
        }, _createSubWidgets: function () {
            try {
                dojo.parser.parse(this.containerNode, true);
            } catch (e) {
                this._onError("Content", e, "Couldn't create widgets in " + this.id + (this.href ? " from " + this.href : ""));
            }
        }, onLoad: function (data) {
        }, onUnload: function () {
        }, onDownloadStart: function () {
            return this.loadingMessage;
        }, onContentError: function (_a6b) {
        }, onDownloadError: function (_a6c) {
            return this.errorMessage;
        }, onDownloadEnd: function () {
        }});
    }
    if (!dojo._hasResource["dijit.form.Form"]) {
        dojo._hasResource["dijit.form.Form"] = true;
        dojo.provide("dijit.form.Form");
        dojo.declare("dijit.form._FormMixin", null, {reset: function () {
            dojo.forEach(this.getDescendants(), function (_a6d) {
                if (_a6d.reset) {
                    _a6d.reset();
                }
            });
        }, validate: function () {
            var _a6e = false;
            return dojo.every(dojo.map(this.getDescendants(), function (_a6f) {
                _a6f._hasBeenBlurred = true;
                var _a70 = _a6f.disabled || !_a6f.validate || _a6f.validate();
                if (!_a70 && !_a6e) {
                    dijit.scrollIntoView(_a6f.containerNode || _a6f.domNode);
                    _a6f.focus();
                    _a6e = true;
                }
                return _a70;
            }), function (item) {
                return item;
            });
        }, setValues: function (val) {
            dojo.deprecated(this.declaredClass + "::setValues() is deprecated. Use attr('value', val) instead.", "", "2.0");
            return this.attr("value", val);
        }, _setValueAttr: function (obj) {
            var map = {};
            dojo.forEach(this.getDescendants(), function (_a75) {
                if (!_a75.name) {
                    return;
                }
                var _a76 = map[_a75.name] || (map[_a75.name] = []);
                _a76.push(_a75);
            });
            for (var name in map) {
                if (!map.hasOwnProperty(name)) {
                    continue;
                }
                var _a78 = map[name], _a79 = dojo.getObject(name, false, obj);
                if (_a79 === undefined) {
                    continue;
                }
                if (!dojo.isArray(_a79)) {
                    _a79 = [_a79];
                }
                if (typeof _a78[0].checked == "boolean") {
                    dojo.forEach(_a78, function (w, i) {
                        w.attr("value", dojo.indexOf(_a79, w.value) != -1);
                    });
                } else {
                    if (_a78[0]._multiValue) {
                        _a78[0].attr("value", _a79);
                    } else {
                        dojo.forEach(_a78, function (w, i) {
                            w.attr("value", _a79[i]);
                        });
                    }
                }
            }
        }, getValues: function () {
            dojo.deprecated(this.declaredClass + "::getValues() is deprecated. Use attr('value') instead.", "", "2.0");
            return this.attr("value");
        }, _getValueAttr: function () {
            var obj = {};
            dojo.forEach(this.getDescendants(), function (_a7f) {
                var name = _a7f.name;
                if (!name || _a7f.disabled) {
                    return;
                }
                var _a81 = _a7f.attr("value");
                if (typeof _a7f.checked == "boolean") {
                    if (/Radio/.test(_a7f.declaredClass)) {
                        if (_a81 !== false) {
                            dojo.setObject(name, _a81, obj);
                        }
                    } else {
                        var ary = dojo.getObject(name, false, obj);
                        if (!ary) {
                            ary = [];
                            dojo.setObject(name, ary, obj);
                        }
                        if (_a81 !== false) {
                            ary.push(_a81);
                        }
                    }
                } else {
                    dojo.setObject(name, _a81, obj);
                }
            });
            return obj;
        }, isValid: function () {
            this._invalidWidgets = [];
            return dojo.every(this.getDescendants(), function (_a83) {
                var _a84 = _a83.disabled || !_a83.isValid || _a83.isValid();
                if (!_a84) {
                    this._invalidWidgets.push(_a83);
                }
                return _a84;
            }, this);
        }, onValidStateChange: function (_a85) {
        }, _widgetChange: function (_a86) {
            var _a87 = this._lastValidState;
            if (!_a86 || this._lastValidState === undefined) {
                _a87 = this.isValid();
                if (this._lastValidState === undefined) {
                    this._lastValidState = _a87;
                }
            } else {
                if (_a86.isValid) {
                    this._invalidWidgets = dojo.filter(this._invalidWidgets || [], function (w) {
                        return (w != _a86);
                    }, this);
                    if (!_a86.isValid() && !_a86.attr("disabled")) {
                        this._invalidWidgets.push(_a86);
                    }
                    _a87 = (this._invalidWidgets.length === 0);
                }
            }
            if (_a87 !== this._lastValidState) {
                this._lastValidState = _a87;
                this.onValidStateChange(_a87);
            }
        }, connectChildren: function () {
            dojo.forEach(this._changeConnections, dojo.hitch(this, "disconnect"));
            var _a89 = this;
            var _a8a = this._changeConnections = [];
            dojo.forEach(dojo.filter(this.getDescendants(), function (item) {
                return item.validate;
            }), function (_a8c) {
                _a8a.push(_a89.connect(_a8c, "validate", dojo.hitch(_a89, "_widgetChange", _a8c)));
                _a8a.push(_a89.connect(_a8c, "_setDisabledAttr", dojo.hitch(_a89, "_widgetChange", _a8c)));
            });
            this._widgetChange(null);
        }, startup: function () {
            this.inherited(arguments);
            this._changeConnections = [];
            this.connectChildren();
        }});
        dojo.declare("dijit.form.Form", [dijit._Widget, dijit._Templated, dijit.form._FormMixin], {name: "", action: "", method: "", encType: "", "accept-charset": "", accept: "", target: "", templateString: "<form dojoAttachPoint='containerNode' dojoAttachEvent='onreset:_onReset,onsubmit:_onSubmit' name='${name}'></form>", attributeMap: dojo.mixin(dojo.clone(dijit._Widget.prototype.attributeMap), {action: "", method: "", encType: "", "accept-charset": "", accept: "", target: ""}), execute: function (_a8d) {
        }, onExecute: function () {
        }, _setEncTypeAttr: function (_a8e) {
            this.encType = _a8e;
            dojo.attr(this.domNode, "encType", _a8e);
            if (dojo.isIE) {
                this.domNode.encoding = _a8e;
            }
        }, postCreate: function () {
            if (dojo.isIE && this.srcNodeRef && this.srcNodeRef.attributes) {
                var item = this.srcNodeRef.attributes.getNamedItem("encType");
                if (item && !item.specified && (typeof item.value == "string")) {
                    this.attr("encType", item.value);
                }
            }
            this.inherited(arguments);
        }, onReset: function (e) {
            return true;
        }, _onReset: function (e) {
            var faux = {returnValue: true, preventDefault: function () {
                this.returnValue = false;
            }, stopPropagation: function () {
            }, currentTarget: e.currentTarget, target: e.target};
            if (!(this.onReset(faux) === false) && faux.returnValue) {
                this.reset();
            }
            dojo.stopEvent(e);
            return false;
        }, _onSubmit: function (e) {
            var fp = dijit.form.Form.prototype;
            if (this.execute != fp.execute || this.onExecute != fp.onExecute) {
                dojo.deprecated("dijit.form.Form:execute()/onExecute() are deprecated. Use onSubmit() instead.", "", "2.0");
                this.onExecute();
                this.execute(this.getValues());
            }
            if (this.onSubmit(e) === false) {
                dojo.stopEvent(e);
            }
        }, onSubmit: function (e) {
            return this.isValid();
        }, submit: function () {
            if (!(this.onSubmit() === false)) {
                this.containerNode.submit();
            }
        }});
    }
    if (!dojo._hasResource["dijit.Dialog"]) {
        dojo._hasResource["dijit.Dialog"] = true;
        dojo.provide("dijit.Dialog");
        dojo.declare("dijit.DialogUnderlay", [dijit._Widget, dijit._Templated], {templateString: "<div class='dijitDialogUnderlayWrapper' id='${id}_wrapper'><div class='dijitDialogUnderlay ${class}' id='${id}' dojoAttachPoint='node'></div></div>", attributeMap: {}, postCreate: function () {
            dojo.body().appendChild(this.domNode);
            this.bgIframe = new dijit.BackgroundIframe(this.domNode);
        }, layout: function () {
            var _a96 = dijit.getViewport();
            var is = this.node.style, os = this.domNode.style;
            os.top = _a96.t + "px";
            os.left = _a96.l + "px";
            is.width = _a96.w + "px";
            is.height = _a96.h + "px";
            var _a99 = dijit.getViewport();
            if (_a96.w != _a99.w) {
                is.width = _a99.w + "px";
            }
            if (_a96.h != _a99.h) {
                is.height = _a99.h + "px";
            }
        }, show: function () {
            this.domNode.style.display = "block";
            this.layout();
            if (this.bgIframe.iframe) {
                this.bgIframe.iframe.style.display = "block";
            }
        }, hide: function () {
            this.domNode.style.display = "none";
            if (this.bgIframe.iframe) {
                this.bgIframe.iframe.style.display = "none";
            }
        }, uninitialize: function () {
            if (this.bgIframe) {
                this.bgIframe.destroy();
            }
        }});
        dojo.declare("dijit._DialogMixin", null, {attributeMap: dijit._Widget.prototype.attributeMap, execute: function (_a9a) {
        }, onCancel: function () {
        }, onExecute: function () {
        }, _onSubmit: function () {
            this.onExecute();
            this.execute(this.attr("value"));
        }, _getFocusItems: function (_a9b) {
            var _a9c = dijit._getTabNavigable(dojo.byId(_a9b));
            this._firstFocusItem = _a9c.lowest || _a9c.first || _a9b;
            this._lastFocusItem = _a9c.last || _a9c.highest || this._firstFocusItem;
            if (dojo.isMoz && this._firstFocusItem.tagName.toLowerCase() == "input" && dojo.attr(this._firstFocusItem, "type").toLowerCase() == "file") {
                dojo.attr(_a9b, "tabindex", "0");
                this._firstFocusItem = _a9b;
            }
        }});
        dojo.declare("dijit.Dialog", [dijit.layout.ContentPane, dijit._Templated, dijit.form._FormMixin, dijit._DialogMixin], {templateString: null, templateString: "<div class=\"dijitDialog\" tabindex=\"-1\" waiRole=\"dialog\" waiState=\"labelledby-${id}_title\">\n\t<div dojoAttachPoint=\"titleBar\" class=\"dijitDialogTitleBar\">\n\t<span dojoAttachPoint=\"titleNode\" class=\"dijitDialogTitle\" id=\"${id}_title\"></span>\n\t<span dojoAttachPoint=\"closeButtonNode\" class=\"dijitDialogCloseIcon\" dojoAttachEvent=\"onclick: onCancel\" title=\"${buttonCancel}\">\n\t\t<span dojoAttachPoint=\"closeText\" class=\"closeText\" title=\"${buttonCancel}\">x</span>\n\t</span>\n\t</div>\n\t\t<div dojoAttachPoint=\"containerNode\" class=\"dijitDialogPaneContent\"></div>\n</div>\n", attributeMap: dojo.mixin(dojo.clone(dijit._Widget.prototype.attributeMap), {title: [
            {node: "titleNode", type: "innerHTML"},
            {node: "titleBar", type: "attribute"}
        ]}), open: false, duration: dijit.defaultDuration, refocus: true, autofocus: true, _firstFocusItem: null, _lastFocusItem: null, doLayout: false, draggable: true, postMixInProperties: function () {
            var _a9d = dojo.i18n.getLocalization("dijit", "common");
            dojo.mixin(this, _a9d);
            this.inherited(arguments);
        }, postCreate: function () {
            var s = this.domNode.style;
            s.visibility = "hidden";
            s.position = "absolute";
            s.display = "";
            s.top = "-9999px";
            dojo.body().appendChild(this.domNode);
            this.inherited(arguments);
            this.connect(this, "onExecute", "hide");
            this.connect(this, "onCancel", "hide");
            this._modalconnects = [];
        }, onLoad: function () {
            this._position();
            this.inherited(arguments);
        }, _endDrag: function (e) {
            if (e && e.node && e.node === this.domNode) {
                var vp = dijit.getViewport();
                var p = e._leftTop || dojo.coords(e.node, true);
                this._relativePosition = {t: p.t - vp.t, l: p.l - vp.l};
            }
        }, _setup: function () {
            var node = this.domNode;
            if (this.titleBar && this.draggable) {
                this._moveable = (dojo.isIE == 6) ? new dojo.dnd.TimedMoveable(node, {handle: this.titleBar}) : new dojo.dnd.Moveable(node, {handle: this.titleBar, timeout: 0});
                dojo.subscribe("/dnd/move/stop", this, "_endDrag");
            } else {
                dojo.addClass(node, "dijitDialogFixed");
            }
            this._underlay = new dijit.DialogUnderlay({id: this.id + "_underlay", "class": dojo.map(this["class"].split(/\s/),function (s) {
                return s + "_underlay";
            }).join(" ")});
            var _aa4 = this._underlay;
            this._fadeIn = dojo.fadeIn({node: node, duration: this.duration, onBegin: dojo.hitch(_aa4, "show")});
            this._fadeOut = dojo.fadeOut({node: node, duration: this.duration, onEnd: function () {
                node.style.visibility = "hidden";
                node.style.top = "-9999px";
                _aa4.hide();
            }});
        }, uninitialize: function () {
            if (this._fadeIn && this._fadeIn.status() == "playing") {
                this._fadeIn.stop();
            }
            if (this._fadeOut && this._fadeOut.status() == "playing") {
                this._fadeOut.stop();
            }
            if (this._underlay) {
                this._underlay.destroy();
            }
            if (this._moveable) {
                this._moveable.destroy();
            }
        }, _size: function () {
            var mb = dojo.marginBox(this.domNode);
            var _aa6 = dijit.getViewport();
            if (mb.w >= _aa6.w || mb.h >= _aa6.h) {
                dojo.style(this.containerNode, {width: Math.min(mb.w, Math.floor(_aa6.w * 0.75)) + "px", height: Math.min(mb.h, Math.floor(_aa6.h * 0.75)) + "px", overflow: "auto", position: "relative"});
            }
        }, _position: function () {
            if (!dojo.hasClass(dojo.body(), "dojoMove")) {
                var node = this.domNode;
                var _aa8 = dijit.getViewport();
                var p = this._relativePosition;
                var mb = p ? null : dojo.marginBox(node);
                dojo.style(node, {left: Math.floor(_aa8.l + (p ? p.l : (_aa8.w - mb.w) / 2)) + "px", top: Math.floor(_aa8.t + (p ? p.t : (_aa8.h - mb.h) / 2)) + "px"});
            }
        }, _onKey: function (evt) {
            if (evt.charOrCode) {
                var dk = dojo.keys;
                var node = evt.target;
                if (evt.charOrCode === dk.TAB) {
                    this._getFocusItems(this.domNode);
                }
                var _aae = (this._firstFocusItem == this._lastFocusItem);
                if (node == this._firstFocusItem && evt.shiftKey && evt.charOrCode === dk.TAB) {
                    if (!_aae) {
                        dijit.focus(this._lastFocusItem);
                    }
                    dojo.stopEvent(evt);
                } else {
                    if (node == this._lastFocusItem && evt.charOrCode === dk.TAB && !evt.shiftKey) {
                        if (!_aae) {
                            dijit.focus(this._firstFocusItem);
                        }
                        dojo.stopEvent(evt);
                    } else {
                        while (node) {
                            if (node == this.domNode) {
                                if (evt.charOrCode == dk.ESCAPE) {
                                    this.onCancel();
                                } else {
                                    return;
                                }
                            }
                            node = node.parentNode;
                        }
                        if (evt.charOrCode !== dk.TAB) {
                            dojo.stopEvent(evt);
                        } else {
                            if (!dojo.isOpera) {
                                try {
                                    this._firstFocusItem.focus();
                                } catch (e) {
                                }
                            }
                        }
                    }
                }
            }
        }, show: function () {
            if (this.open) {
                return;
            }
            if (!this._alreadyInitialized) {
                this._setup();
                this._alreadyInitialized = true;
            }
            if (this._fadeOut.status() == "playing") {
                this._fadeOut.stop();
            }
            this._modalconnects.push(dojo.connect(window, "onscroll", this, "layout"));
            this._modalconnects.push(dojo.connect(window, "onresize", this, "layout"));
            this._modalconnects.push(dojo.connect(dojo.doc.documentElement, "onkeypress", this, "_onKey"));
            dojo.style(this.domNode, {opacity: 0, visibility: ""});
            this.open = true;
            this._loadCheck();
            this._size();
            this._position();
            this._fadeIn.play();
            this._savedFocus = dijit.getFocus(this);
            if (this.autofocus) {
                this._getFocusItems(this.domNode);
                setTimeout(dojo.hitch(dijit, "focus", this._firstFocusItem), 50);
            }
        }, hide: function () {
            if (!this._alreadyInitialized) {
                return;
            }
            if (this._fadeIn.status() == "playing") {
                this._fadeIn.stop();
            }
            this._fadeOut.play();
            if (this._scrollConnected) {
                this._scrollConnected = false;
            }
            dojo.forEach(this._modalconnects, dojo.disconnect);
            this._modalconnects = [];
            if (this.refocus) {
                this.connect(this._fadeOut, "onEnd", dojo.hitch(dijit, "focus", this._savedFocus));
            }
            if (this._relativePosition) {
                delete this._relativePosition;
            }
            this.open = false;
        }, layout: function () {
            if (this.domNode.style.visibility != "hidden") {
                this._underlay.layout();
                this._position();
            }
        }, destroy: function () {
            dojo.forEach(this._modalconnects, dojo.disconnect);
            if (this.refocus && this.open) {
                setTimeout(dojo.hitch(dijit, "focus", this._savedFocus), 25);
            }
            this.inherited(arguments);
        }});
        dojo.declare("dijit.TooltipDialog", [dijit.layout.ContentPane, dijit._Templated, dijit.form._FormMixin, dijit._DialogMixin], {title: "", doLayout: false, autofocus: true, "class": "dijitTooltipDialog", _firstFocusItem: null, _lastFocusItem: null, templateString: null, templateString: "<div waiRole=\"presentation\">\n\t<div class=\"dijitTooltipContainer\" waiRole=\"presentation\">\n\t\t<div class =\"dijitTooltipContents dijitTooltipFocusNode\" dojoAttachPoint=\"containerNode\" tabindex=\"-1\" waiRole=\"dialog\"></div>\n\t</div>\n\t<div class=\"dijitTooltipConnector\" waiRole=\"presentation\"></div>\n</div>\n", postCreate: function () {
            this.inherited(arguments);
            this.connect(this.containerNode, "onkeypress", "_onKey");
            this.containerNode.title = this.title;
        }, orient: function (node, _ab0, _ab1) {
            this.domNode.className = this["class"] + " dijitTooltipAB" + (_ab1.charAt(1) == "L" ? "Left" : "Right") + " dijitTooltip" + (_ab1.charAt(0) == "T" ? "Below" : "Above");
        }, onOpen: function (pos) {
            this.orient(this.domNode, pos.aroundCorner, pos.corner);
            this._loadCheck();
            if (this.autofocus) {
                this._getFocusItems(this.containerNode);
                dijit.focus(this._firstFocusItem);
            }
        }, _onKey: function (evt) {
            var node = evt.target;
            var dk = dojo.keys;
            if (evt.charOrCode === dk.TAB) {
                this._getFocusItems(this.containerNode);
            }
            var _ab6 = (this._firstFocusItem == this._lastFocusItem);
            if (evt.charOrCode == dk.ESCAPE) {
                this.onCancel();
                dojo.stopEvent(evt);
            } else {
                if (node == this._firstFocusItem && evt.shiftKey && evt.charOrCode === dk.TAB) {
                    if (!_ab6) {
                        dijit.focus(this._lastFocusItem);
                    }
                    dojo.stopEvent(evt);
                } else {
                    if (node == this._lastFocusItem && evt.charOrCode === dk.TAB && !evt.shiftKey) {
                        if (!_ab6) {
                            dijit.focus(this._firstFocusItem);
                        }
                        dojo.stopEvent(evt);
                    } else {
                        if (evt.charOrCode === dk.TAB) {
                            evt.stopPropagation();
                        }
                    }
                }
            }
        }});
    }
    dojo.i18n._preloadLocalizations("dojo.nls.dojo", ["he", "nl", "tr", "ko", "el", "en", "en-gb", "ROOT", "zh-cn", "hu", "es", "fi-fi", "pt-br", "ca", "fi", "he-il", "xx", "ru", "it", "fr", "cs", "de-de", "fr-fr", "it-it", "es-es", "ja", "sk", "da", "sl", "pl", "de", "sv", "pt", "pt-pt", "nl-nl", "zh-tw", "ko-kr", "ar", "en-us", "zh", "nb", "th", "ja-jp"]);
    if (dojo.config.afterOnLoad && dojo.isBrowser) {
        window.setTimeout(dojo._fakeLoadInit, 1000);
    }
})();
