<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script type="text/javascript"  src="js/a076d05399.js"></script>
<script type="text/javascript"	src="js/onclick.js"></script>
<script type="text/javascript"	src="js/getOidcClient.js"></script>
<script type="text/javascript"	src="js/getSigningCert.js"></script>
<script type="text/javascript"  src="js/jquery-latest.min.js"></script>
<script type="text/javascript"  src="js/jquery.min.js"></script>
<script type="text/javascript"  src="js/jquery-ui.min.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css"/>
<link rel="shortcut icon" href="favicon.ico" />
<title>PING Objects Migration</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<header>
		<h2>PING Objects Migration</h2>
		<div class="dropdown">
		<ul>
           <li class='login'>${sessionScope.username}
              <div class="dropdown-content">
                <ul>
                    <li class='login'><a href='${pageContext.request.contextPath}/Logout'>Logout</a></li>
                </ul>
                </div>
            </li>
            </ul>
          </div>
	</header>
	
	
	<div id="sidebar">
<!-- 		<div class="toggle-btn" onclick="toggleSideBar()">
			<span></span> <span></span> <span></span>
		</div> -->
		<div id="nav">
			<ul>
				<li class=""><a href="#about">About</a></li>
				<li class="">
					<a href="#" class="pf-btn">PingFederate Objects 
						<span class="fas fa-caret-down first"></span>
					</a>
					<ul class="pf-show">
						<li><a href="#pfoidcObjects">OIDC Objects</a></li>
						<li><a href="#pfsamlObjects">SAML Objects</a></li>
					</ul>
				</li>
				<li class=""><a href="#paobjects">PingAccess Objects</a></li>
			</ul>
		</div>
		<div class="copyright">
		      Copyright © 2020 <br>
		      Optum Technology
		</div>
	</div>



	<div class="container">

		<div id="about" class="toggle" style="display: none">
			<p>PING Object Migration application is developed to Migrate the
				objects from lower environments to higher environments. For eg.
				Development to Staging and then Production</p>
		</div>

		<div id="pfoidcObjects" class="toggle" style="display: none">
			<form id="pfform" name="pfform" action="PFOidcObjects" method="post">
				<div class="row">
					<div class="col-25">
						<label for="env">Source Environment</label>
					</div>
					<div class="col-75">
						<label> 
						<input type="radio" id="sourceenv" name="sourceenv" value="dev" onclick="getOidcClients()" /> Dev	</label> 
						<label> 
						<input type="radio" id="sourceenv" name="sourceenv" value="stage" onclick="getOidcClients()" /> Stage	</label> 
						<label> 
						<input type="radio" id="sourceenv" name="sourceenv" value="prod" disabled /> Production
						</label>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="env">Destination Environment</label>
					</div>
					<div class="col-75">
						<label> <input type="radio" name="destenv" value="sandbox" />
							Sandbox
						</label> <label> <input type="radio" name="destenv" value="dev" />
							Dev
						</label> <label> <input type="radio" name="destenv" value="stage" />
							Stage
						</label> <label> <input type="radio" name="destenv" value="prod"
							disabled /> Production
						</label>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="sourceclientid">Source ClientID</label>
					</div>
					<div class="col-75">
						<input type="text" id="sourceclientid" name="sourceclientid" required />
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="destclientid">Destination ClientID</label>
					</div>
					<div class="col-75">
						<input type="text" name="destclientid"	required />
					</div>
				</div>
				
				<div class="row">
					<div class="col-25">
						<label for="clientsecret">Client Secret</label>
					</div>
					<div class="col-75">
						<input type="text" id="clientsecret" name="clientsecret" required /> <button id="btn" type="button" onClick="generateUUID()" >Generate Secret</button>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="redirecturl">Redirect URL</label>
					</div>
					<div id=redirectdiv class="col-75">
						<input type="url" id="redirectUrl" name="redirecturl[]" required /> <button id="btn" type="button" onClick="addInputField()" >Add</button>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="sourceoidcPolicy">Source OIDC Policy</label>
					</div>
					<div class="col-75">
						<input type="text" id="sourceoidcPolicy" name="sourceoidcPolicy" required />
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="destoidcPolicy">Destination OIDC Policy</label>
					</div>
					<div class="col-75">
						<input type="text" name="destoidcPolicy"
							required />
					</div>
				</div>
				<br>
				<br>
				<button>Create</button>
			</form>
		</div>
		
		<div id="pfsamlObjects" class="toggle" style="display: none">
			<form action="PFSamlObjects" method="post" enctype="multipart/form-data">
			<div class="row">
					<div class="col-25">
						<label for="env">Source Environment</label>
					</div>
					<div class="col-75">
						<label> 
						<input type="radio" id="sourceenv" name="sourceenv" value="dev" /> Dev	</label> 
						<label> 
						<input type="radio" id="sourceenv" name="sourceenv" value="stage" /> Stage	</label> 
						<label> 
						<input type="radio" id="sourceenv" name="sourceenv" value="prod" disabled /> Production
						</label>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="env">Destination Environment</label>
					</div>
					<div class="col-75" >
						<label> <input type="radio" name="destenv" value="sandbox" />
							Sandbox
						</label> <label> <input type="radio" name="destenv" value="dev" onclick="getSigningCerts()" />
							Dev
						</label> <label> <input type="radio" name="destenv" value="stage" onclick="getSigningCerts()" />
							Stage
						</label> <label> <input type="radio" name="destenv" value="prod"
							disabled /> Production
						</label>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="sourcespid">Source SP ID</label>
					</div>
					<div class="col-75">
						<input type="text" id="sourcespid" name="sourcespid" required /> <button id="btn" type="button" onClick="validateSPID()" >Validate</button> 
						  <div class='msg-input' id='msg-input' style='color:red;margin-top:5px;margin-bottom: 5px;display:none;'>Please enter SP ID</div>
						  <div class='msg-success' id='msg-success' style='color:green;margin-top:5px;margin-bottom: 5px;display:none;'>SP ID found</div>
						  <div class='msg-error' id='msg-error' style='color:red;margin-top:5px;margin-bottom: 5px;display:none;'>SP ID not found</div>
					</div>
				</div>
				
				<div class="row">
					<div class="col-25">
						<label for="destspconn">Destination SP Connection</label>
					</div>
					<div class="col-75">
						<input type="text" name="destspconn"	required />
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="destspid">Destination SP ID</label>
					</div>
					<div class="col-75">
						<input type="text" name="destspid"	required />
					</div>
				</div>
				
				<div class="row">
					<div class="col-25">
						<label for="destacs">Destination ACS URL</label>
					</div>
					<div id="acsdiv" class="col-75">
						<input type="text" id="destacs" name="destacs[]" required /> <button id="btn" type="button" onClick="addACS()" >Add</button>
					</div>
				</div>
				
				<div class="row">
					<div class="col-25">
						<label for="signingcert">Signing Certificate</label>
					</div>
					<div class="col-75">
						<input type="text" id="signingcert" name="signingcert" required />
					</div>
				</div>
				
				<div id="encrypt" class="row">
					<div class="col-25">
						<label for="destacs">Encryption Required </label>
					</div>
					<div class="col-75">
						<label> <input type="radio" name="isencryption" class="rad" value="yes" />
							Yes
						</label> <label> <input type="radio" name="isencryption" class="rad" value="no" />
							No
						</label> <label> <input type="radio" name="isencryption" class="rad" value="sourceyes" />
							Yes (Use from Source)
						</label>
					</div>
				</div>
				
				<div id="upload" class="row" style="display: none">
					<div class="col-25">
						<label>CertFile (Base64 Format) </label>
					</div>
					<div class="col-75">
						<label><input type="file" name="file"/> </label>
					</div>
				</div>
				
				<br>
				<br>
				<button>Create</button>
			</form>
		</div>
		
		<div id="paobjects" class="toggle" style="display: none">
			<form action="PAObjects" method="post">
				<div class="row">
					<div class="col-25">
						<label for="env">Source Environment</label>
					</div>
					<div class="col-75">
						<label> <input type="radio" name="sourceenv" value="dev"
							 /> Dev
						</label> <label> <input type="radio" name="sourceenv"
							value="stage" /> Stage
						</label> <label> <input type="radio" name="sourceenv" value="prod"
							disabled /> Production
						</label>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="env">Destination Environment</label>
					</div>
					<div class="col-75">
						<label> <input type="radio" name="destenv" value="sandbox" onclick="getClients()"/>
							Sandbox
						</label> <label> <input type="radio" name="destenv" value="dev" onclick="getClients()"/>
							Dev
						</label> <label> <input type="radio" name="destenv" value="stage" onclick="getClients()"/>
							Stage
						</label> <label> <input type="radio" name="destenv" value="prod"
							disabled /> Production
						</label>
					</div>
				</div>
				
				<div class="row">
					<div class="col-25">
						<label for="env">Region</label>
					</div>
					<div class="col-75">
						<label> <input type="radio" name="region" value="R1" />	R1 </label> 
						<label> <input type="radio" name="region" value="R2" />	R2 </label> 
						<label> <input type="radio" name="region" value="R3" /> R3 </label>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="sourceapp">Source Application</label>
					</div>
					<div class="col-75">
						<input type="text" name="sourceapp" required />
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="virtualhosts">Virtual Hosts</label>
					</div>
					<div id="vhdiv" class="col-75">
						<input type="text" id="vh" name="virtualhosts[]" required /> <button id="btn" type="button" onClick="addVH()" >Add</button>
					</div>
				</div>

				<div class="row">
					<div class="col-25">
						<label for="site">Backend Site</label>
					</div>
					<div id="sitediv" class="col-75">
						<input type="text" id="site" name="site[]" placeholder= "hostname:port" required /> <button id="btn" type="button" onClick="addSite()" >Add</button>
					</div>
				</div>
				
				<div class="row">
					<div class="col-25">
						<label for="clientid">WebSession</label>
					</div>
					<div id="input" class="col-75">
						<input type="text" id="clientid" name="clientid" placeholder="clientID" required />
						<input type="text" id="aud" name="aud" placeholder="audience" required />
					</div>
				</div>
				
				<div class="row">
					<div class="col-25">
						<label for="destobjname">Destination Objects Name</label>
					</div>
					<div class="col-75">
						<input type="text" name="destobjname" required />
					</div>
				</div>
				
				<br>
				<br>
				<button>Create</button>
			</form>
		</div>
		
	</div>
</body>

<script type="text/javascript">
function toggleSideBar(){
	document.getElementById("sidebar").classList.toggle('active');
}

function loadContent(selector){
	$("#loadOnClick").html($(selector).html());
};


$("#nav a").click(function(e){
    e.preventDefault();
    $(".toggle").hide();
    var toShow = $(this).attr('href');
    $(toShow).show();
});

/* $('.active-menu').on('click','li', function(){
    $(this).addClass('active').siblings().removeClass('active');
  }); */

$('.pf-btn').click(function(){
	$('#sidebar ul .pf-show').toggleClass("show");
	$('#sidebar ul .first').toggleClass("rotate");
});

$('#sidebar ul li').click(function(){
	$(this).addClass('active').siblings().removeClass('active');
	$(this).siblings().find('li').removeClass('active');
});

$(".rad").click(function() {
    if($('.rad:checked').val() != "yes"){
    	$('#upload').hide();
        }
    else{
    	$('#upload').show();
    	}
})

</script>
</html>