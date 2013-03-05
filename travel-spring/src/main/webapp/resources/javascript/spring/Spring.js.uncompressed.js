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

Spring.addDecoration = function (/*Object*/decoration) {
    //Spring.decorations.push(decoration);

    if (!Spring.decorations[decoration.elementId]) {
        Spring.decorations[decoration.elementId] = [];
        Spring.decorations[decoration.elementId].push(decoration);
    } else {
        var replaced = false;
        for (var i = 0; i < Spring.decorations[decoration.elementId].length; i++) {
            var existingDecoration = Spring.decorations[decoration.elementId][i];
            if (existingDecoration.equals(decoration)) {
                if (existingDecoration.cleanup != undefined) {
                    existingDecoration.cleanup();
                }
                Spring.decorations[decoration.elementId][i] = decoration;
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            Spring.decorations[decoration.elementId].push(decoration);
        }
    }

    if (Spring.decorations.applied) {
        decoration.apply();
    }
};

Spring.applyDecorations = function () {
    if (!Spring.decorations.applied) {
        for (var elementId in Spring.decorations) {
            for (var x = 0; x < Spring.decorations[elementId].length; x++) {
                Spring.decorations[elementId][x].apply();
            }
        }
        Spring.decorations.applied = true;
    }
};

Spring.validateAll = function () {
    var valid = true;
    for (var elementId in Spring.decorations) {
        for (var x = 0; x < Spring.decorations[elementId].length; x++) {
            if (Spring.decorations[elementId][x].widget && !Spring.decorations[elementId][x].validate()) {
                valid = false;
            }
        }
    }
    return valid;
};

Spring.validateRequired = function () {
    var valid = true;
    for (var elementId in Spring.decorations) {
        for (var x = 0; x < Spring.decorations[elementId].length; x++) {
            if (Spring.decorations[elementId][x].widget && Spring.decorations[elementId][x].isRequired() && !Spring.decorations[elementId][x].validate()) {
                valid = false;
            }
        }
    }
    return valid;
};

Spring.AbstractElementDecoration = function () {
};

Spring.AbstractElementDecoration.prototype = {

    elementId: "",
    widgetType: "",
    widgetModule: "",
    widget: null,
    widgetAttrs: {},

    apply: function () {
    },

    validate: function () {
    },

    isRequired: function () {
    },

    equals: function (/*Object*/other) {
    }
};

Spring.AbstractValidateAllDecoration = function () {
};

Spring.AbstractValidateAllDecoration.prototype = {

    event: "",
    elementId: "",

    apply: function () {
    },

    cleanup: function () {
    },

    handleEvent: function (event) {
    },

    equals: function (/*Object*/other) {
    }
};

Spring.AbstractCommandLinkDecoration = function () {
};

Spring.AbstractCommandLinkDecoration.prototype = {

    elementId: "",
    linkHtml: "",

    apply: function () {
    },

    submitFormFromLink: function (/*String*/ formId, /*String*/ sourceId, /*Array of name,value params*/ params) {
    },

    equals: function (/*Object*/other) {
    }
};

Spring.AbstractAjaxEventDecoration = function () {
};

Spring.AbstractAjaxEventDecoration.prototype = {

    event: "",
    elementId: "",
    sourceId: "",
    formId: "",
    popup: false,
    params: {},

    apply: function () {
    },

    cleanup: function () {
    },

    submit: function (event) {
    },

    equals: function (/*Object*/other) {
    }
};

Spring.AbstractRemotingHandler = function () {
};

Spring.AbstractRemotingHandler.prototype = {

    submitForm: function (/*String */ sourceId, /*String*/formId, /*Object*/ params) {
    },

    getLinkedResource: function (/*String */ linkId, /*Object*/params, /*boolean*/ modal) {
    },

    getResource: function (/*String */ resourceUri, /*Object*/params, /*boolean*/ modal) {
    },

    handleResponse: function () {
    },

    handleError: function () {
    }
};
