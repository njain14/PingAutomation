function getSigningCerts() {
	var certValue = [];
	var envByName = document.getElementsByName('destenv');
	$('#signingcert').empty();

	for (var j = 0; j < envByName.length; j++) {
		if (envByName[j].checked) {
			$
					.get(
							'/PingObjectMigration/GetIDPCertificates',
							{
								"env" : envByName[j].value,
								"path" : "/keyPairs/signing"
							},
							function(certres) {
								var certResponse = JSON.parse(certres);
								for (var i = 0; i < Object
										.keys(certResponse.items).length; i++) {
									var x = certResponse.items[i];
									certValue[i] = x.subjectDN.split(",",1) + " "  +  x.id;
									
								}
								$('#signingcert').autocomplete({
									source : certValue,
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