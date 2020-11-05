function addInputField() {
	var div = document.getElementById('redirectdiv');
	if (document.getElementById('redirectUrl').value != "") {
		var input = document.createElement("input");
		input.type = 'url';
		input.name = 'redirecturl[]';
		input.id = 'redirectUrl';
		div.appendChild(input);
	}
}

function generateUUID() {
	var clientsec = document.getElementById('clientsecret');
	clientsec.value = ([1e7]+1e3+4e3+8e3+1e11+[1e7]+1e3+4e3+8e3+1e11).replace(/[018]/g, c =>
    (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16));
}

function validateSPID() {
	var envByName = document.getElementsByName('sourceenv');
	var spidVal = document.getElementById('sourcespid').value;
	var spid    = document.getElementById('sourcespid');
	if(!(spid.value.length))
    {
	   document.getElementById('msg-error').style.display = 'none';
	   document.getElementById('msg-success').style.display = 'none';
       document.getElementById('msg-input').style.display = 'block';
       event.preventDefault();
       spid.focus();
       
    }
	
	else {
       for (var j = 0; j < envByName.length; j++) {
		if (envByName[j].checked) {
			$
					.get(
							'/PingObjectMigration/Validation',
							{
								"env" : envByName[j].value,
								"path" : "/idp/spConnections",
								"spid" : spidVal
							},
							function(validationres) {
								if (validationres == "Success") {
									 document.getElementById('msg-input').style.display = 'none';
									 document.getElementById('msg-error').style.display = 'none';
									 document.getElementById('msg-success').style.display = 'block';
								     event.preventDefault();
								} else {
									 document.getElementById('msg-input').style.display = 'none';
									 document.getElementById('msg-success').style.display = 'none';
									 document.getElementById('msg-error').style.display = 'block';
								     event.preventDefault();
								     spid.focus();
								}
							});
			}
       }
    }
}

function addVH() {
	var div = document.getElementById('vhdiv');
	if (document.getElementById('vh').value != "") {
		var input = document.createElement("input");
		input.type = 'text';
		input.name = 'virtualhosts[]';
		input.id = 'vh';
		div.appendChild(input);
	}
}

function addSite() {
	var div = document.getElementById('sitediv');
	if (document.getElementById('site').value != "") {
		var input = document.createElement("input");
		input.type = 'text';
		input.name = 'site[]';
		input.id = 'site';
		div.appendChild(input);
	}
}

function addACS() {
	var div = document.getElementById('acsdiv');
	if (document.getElementById('destacs').value != "") {
		var input = document.createElement("input");
		input.type = 'text';
		input.name = 'destacs[]';
		input.id = 'destacs';
		div.appendChild(input);
	}
}

function validateInputField() {
	var value = [];
	var site = document.forms["paform"]["site"];
	console.log(site.value);
	for (i = 0; i < site.length; i++) {
		if (site[i].split(":", 2) == null) {
			window.alert("Please enter port.");
			site.focus();
			return false;
		}
	}
}