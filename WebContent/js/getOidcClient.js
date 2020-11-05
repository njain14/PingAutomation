function getOidcClients() {
	var clientvalues = [];
	var policyvalues = [];
	var envByName = document.getElementsByName('sourceenv');
	$('#sourceclientid').empty();
	$('#sourceoidcPolicy').empty();

	for (var j = 0; j < envByName.length; j++) {
		if (envByName[j].checked) {
			$
					.get(
							'/PingObjectMigration/GetClients',
							{
								"env" : envByName[j].value,
								"path" : "/oauth/clients"
							},
							function(clientres) {
								var clientResponse = JSON.parse(clientres);
								for (var i = 0; i < Object
										.keys(clientResponse.items).length; i++) {
									var x = clientResponse.items[i];
									clientvalues[i] = x.clientId;
								}
								$('#sourceclientid').autocomplete({
									source : clientvalues,
									autoFocus : true,
									minLength : 0,
									scroll : true,
								}).bind('focus', function() {
									$(this).autocomplete("search");
								});
							});

			$
					.get(
							'/PingObjectMigration/GetClients',
							{
								"env" : envByName[j].value,
								"path" : "/oauth/openIdConnect/policies"
							},
							function(policyres) {
								var clientResponse = JSON.parse(policyres);
								for (var m = 0; m < Object
										.keys(clientResponse.items).length; m++) {
									var x = clientResponse.items[m];
									policyvalues[m] = x.id;
								}
								$('#sourceoidcPolicy').autocomplete({
									source : policyvalues,
									autoFocus : true,
									minLength : 0,
									scroll : true,
								}).bind('focus', function() {
									$(this).autocomplete("search");
								});
							});
		}
	}
}

function getClients() {
	var clientvalues = [];
	var envByName = document.getElementsByName('destenv');
	$('#clientid').empty();

	for (var j = 0; j < envByName.length; j++) {
		if (envByName[j].checked) {
			$
					.get(
							'/PingObjectMigration/GetClients',
							{
								"env" : envByName[j].value,
								"path" : "/oauth/clients"
							},
							function(clientres) {
								var clientResponse = JSON.parse(clientres);
								for (var i = 0; i < Object
										.keys(clientResponse.items).length; i++) {
									var x = clientResponse.items[i];
									clientvalues[i] = x.clientId;
								}
								$('#clientid').autocomplete({
									source : clientvalues,
									autoFocus : true,
									minLength : 0,
									scroll : true,
								}).bind('focus', function() {
									$(this).autocomplete("search");
								});
							});
		}
	}
}