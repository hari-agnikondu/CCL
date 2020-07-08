
function addMissingId() {
	var elementsToAddId = [ 'input', 'select', 'textarea' ];
	if (elementsToAddId.length < 200) {
		for (var j = 0; j < elementsToAddId.length; j++) {
			var inputElements = document
					.getElementsByTagName(elementsToAddId[j]);
			for (var i = 0; i < inputElements.length; i++) {
				includeIdIfNotExist(inputElements[i]);
			}
		}
	}
}

function removeCustomError(Field) {
	addMissingId();
	if (window.DOMParser) {
		var element = document.getElementById('lbl' + Field);
		if (element != null) {
			var br = element.previousElementSibling;
			if (br != null) {
				br.parentNode.removeChild(br);
			}
			element.parentNode.removeChild(element);
		}

	} else {
		if (document.getElementById(Field).parentNode.innerHTML
				.indexOf('<BR><LABEL class=required_errmsg>') > 0) {
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML.substring(0,
					document.getElementById(Field).parentNode.innerHTML
							.indexOf('<BR><LABEL class=required_errmsg>'));
		}

	}
}

function includeIdIfNotExist(element) {
	var id = element.getAttribute('id');
	var name = element.getAttribute('name');
	if (name && !id) {
		element.id = name;
	}
}

function customError(Field, msg) {
	//alert('came inside');
	addMissingId();
	if (window.DOMParser) {
		var element = document.getElementById('lbl' + Field);
		if (element == null) {
			document.getElementById(Field).parentNode.insertAdjacentHTML(
					'beforeend', '<br><font color="red"><label  id="lbl' + Field
							+ '" class="required_errmsg">' + msg + "</label></font>");
		} else {
			 element = document.getElementById('lbl' + Field); // will
																	// return
																	// element
			if (element != null) {
				var br = element.previousElementSibling;
				if (br != null) {
					br.parentNode.removeChild(br);
				}
				element.parentNode.removeChild(element);
			}
			document.getElementById(Field).parentNode.insertAdjacentHTML(
					'beforeend', '<br><label  id="lbl' + Field
							+ '" class="required_errmsg">' + msg + "</label>");
		}
	}

	else {
		if (document.getElementById(Field).parentNode.innerHTML
				.indexOf('<BR><LABEL id=lbl' + Field
						+ ' class=required_errmsg>') == -1) {
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML
					+ '<br><label id=lbl'
					+ Field
					+ ' class=required_errmsg>'
					+ msg + '</label>';
		} else {
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML.substring(0,
					document.getElementById(Field).parentNode.innerHTML
							.indexOf('<BR><LABEL id=lbl' + Field
									+ ' class=required_errmsg>'));
			document.getElementById(Field).parentNode.innerHTML = document
					.getElementById(Field).parentNode.innerHTML
					+ '<BR><LABEL id=lbl'
					+ Field
					+ ' class=required_errmsg>'
					+ msg + "</label>";
		}
	}

}
