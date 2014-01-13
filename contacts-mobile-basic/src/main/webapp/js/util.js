/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

APPMODULE.namespace('APPMODULE.util.getCurrentDate');
APPMODULE.namespace('APPMODULE.util.getCurrentTime');
APPMODULE.namespace('APPMODULE.util.getCurrentDateTime');
APPMODULE.namespace('APPMODULE.util.convertUTCToDate');

/**
 * Abstract away generic functions that are used by all.
 * 
 * @author Joshua Wilson
 */
APPMODULE.util.getCurrentDate = function() {
    var d = new Date();
    var month = d.getMonth()+1;
    var day = d.getDate();

    var output = d.getFullYear() + '-' +
        (month<10 ? '0' : '') + month + '-' +
        (day<10 ? '0' : '') + day;
    
    return output;
};

APPMODULE.util.getCurrentTime = function() {
    var d = new Date();
    var hour = d.getHours();
    var min = d.getMinutes();
    var sec = d.getSeconds();
    var millisec = d.getMilliseconds();
    
    var output = (hour<10 ? '0' : '') + hour + ":" + 
                 (min<10 ? '0' : '') + min + ":" + 
                 (sec<10 ? '0' : '') + sec + "," + 
                 (millisec<10 ? '0' : (millisec<100 ? '0' : '')) + millisec;
    
    return output;
};

APPMODULE.util.getCurrentDateTime = function() {
    var output = APPMODULE.util.getCurrentDate() + ' ' + APPMODULE.util.getCurrentTime();
    return output;
};

/**
 * The database stores the Date in Milliseconds from the epoch.  We need to convert that into a readable date.
 */
APPMODULE.util.convertUTCToDate = function(milliseconds) {
	console.log(APPMODULE.util.getCurrentTime() + " [js/util.js] (convertUTCToDate) - milliseconds passed in = " + milliseconds);
	var d = new Date(milliseconds);
	console.log(APPMODULE.util.getCurrentTime() + " [js/util.js] (convertUTCToDate) - date after converting milliseconds passed in = " + d);
	
    var month = d.getMonth()+1;
    var day = d.getDate();

    var output = d.getFullYear() + '-' +
        (month<10 ? '0' : '') + month + '-' +
        (day<10 ? '0' : '') + day;
    
    return output;
};

