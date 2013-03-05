/*
 * Copyright (C) 2009 Raivo Laanemets <rl@starline.ee>
 *
 * This file is part of rl-blog.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rl-blog.  If not, see <http://www.gnu.org/licenses/>.
 */
/* Most code from pastebin.com */

var modified = false;

function checkModified() {
	if (modified) {
		alert("Entry contents is modified, save first");
		return false;
	}

	return true;
}

function onTextareaKey(item, e) {
	modified = true;

	if (navigator.userAgent.match("Gecko")) {
		c = e.which;
	} else {
		c = e.keyCode;
	}
	if (c == 9) {
		replaceSelection(item, String.fromCharCode(9));
		setTimeout("document.getElementById('" + item.id + "').focus();", 0);
		return false;
	}
}

function setSelectionRange(input, selectionStart, selectionEnd) {
	if (input.setSelectionRange) {
		input.focus();
		input.setSelectionRange(selectionStart, selectionEnd);
	} else if (input.createTextRange) {
		var range = input.createTextRange();
		range.collapse(true);
		range.moveEnd('character', selectionEnd);
		range.moveStart('character', selectionStart);
		range.select();
	}
}

/* Code contributed by Paul Brennan */
function replaceSelection(input, replaceString) {
	if (input.setSelectionRange) {
		var selectionStart = input.selectionStart;
		var selectionEnd = input.selectionEnd;
		input.value = input.value.substring(0, selectionStart) + replaceString
				+ input.value.substring(selectionEnd);

		if (selectionStart != selectionEnd) {
			setSelectionRange(input, selectionStart, selectionStart
					+ replaceString.length);
		} else {
			setSelectionRange(input, selectionStart + replaceString.length,
					selectionStart + replaceString.length);
		}

	} else if (document.selection) {
		var range = document.selection.createRange();

		if (range.parentElement() == input) {
			var isCollapsed = range.text == '';
			range.text = replaceString;

			if (!isCollapsed) {
				range.moveStart('character', -replaceString.length);
				range.select();
			}
		}
	}
}

/* Code from http://www.codeproject.com/KB/scripting/Session_Timeout.aspx */
var sessionTimeout = 0;

function startSessionTimeoutDisplay(sessionTimeoutValue) {
	sessionTimeout = sessionTimeoutValue;
	window.setTimeout("displaySessionTimeout()", 1000);
}

function displaySessionTimeout() {
	
	document.getElementById("sessionTimeoutDisplay").innerHTML = "Session expires in " + sessionTimeout + " minutes!";
	sessionTimeout = sessionTimeout - 1;

	if (sessionTimeout >= 0)
		window.setTimeout("displaySessionTimeout()", 60000);
	else {
		alert("Session expired, data cannot be submit anymore.");
		document.getElementById("sessionTimeoutDisplay").innerHTML = "Session has expired! Submit will lose data.";
	}
}