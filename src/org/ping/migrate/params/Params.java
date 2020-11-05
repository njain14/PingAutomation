package org.ping.migrate.params;

import javax.servlet.http.Part;

public class Params {

	public static String sourceenv;
	public static String destinationenv;
	public static String sourceclientid;
	public static String destinationclientid;
	public static String sourceoidcPolicy;
	public static String destinationoidcPolicy;
	public static String region;
	public static String[] redirectURL;
	public static String sourceApplication;
	public static String[] virtualHosts;
	public static String[] sites;
	public static String destinationObjectName;
	public static String sourcespid;
	public static String destspid;
	public static String[] destacs;
	public static Part certFile;
	public static String signCertID;
	public static String destinationspconn;
	private static String wsClientid;
	private static String wsAud;
	private static String clientSecret;
	

	public static String getClientSecret() {
		return clientSecret;
	}

	public static void setClientSecret(String clientSecret) {
		Params.clientSecret = clientSecret;
	}

	public static String getWsClientid() {
		return wsClientid;
	}

	public static void setWsClientid(String wsClientid) {
		Params.wsClientid = wsClientid;
	}

	public static String getWsAud() {
		return wsAud;
	}

	public static void setWsAud(String wsAud) {
		Params.wsAud = wsAud;
	}

	public static String getSignCertID() {
		return signCertID;
	}

	public static void setSignCertID(String signCertID) {
		Params.signCertID = signCertID;
	}

	public static Part getCertFile() {
		return certFile;
	}

	public static void setCertFile(Part certFile) {
		Params.certFile = certFile;
	}

	public static String getSourcespid() {
		return sourcespid;
	}

	public static void setSourcespid(String sourcespid) {
		Params.sourcespid = sourcespid;
	}

	public static String getDestspid() {
		return destspid;
	}

	public static void setDestspid(String destspid) {
		Params.destspid = destspid;
	}
		
	
	public static String[] getDestacs() {
		return destacs;
	}

	public static void setDestacs(String[] destacs) {
		Params.destacs = destacs;
	}

	public static String getDestinationspconn() {
		return destinationspconn;
	}

	public static void setDestinationspconn(String destinationspconn) {
		Params.destinationspconn = destinationspconn;
	}

	public static String getSourceApplication() {
		return sourceApplication;
	}

	public static void setSourceApplication(String sourceApplication) {
		Params.sourceApplication = sourceApplication;
	}

	public static String getDestinationObjectName() {
		return destinationObjectName;
	}

	public static void setDestinationObjectName(String destinationObjectName) {
		Params.destinationObjectName = destinationObjectName;
	}

	public static String[] getRedirectURL() {
		return redirectURL;
	}

	public static void setRedirectURL(String[] redirectURL) {
		Params.redirectURL = redirectURL;
	}

	public static String[] getVirtualHosts() {
		return virtualHosts;
	}

	public static void setVirtualHosts(String[] virtualHosts) {
		Params.virtualHosts = virtualHosts;
	}

	public static String[] getSites() {
		return sites;
	}

	public static void setSites(String[] sites) {
		Params.sites = sites;
	}

	public static String getSourceenv() {
		return sourceenv;
	}

	public static void setSourceenv(String sourceenv) {
		Params.sourceenv = sourceenv;
	}

	public static String getDestinationenv() {
		return destinationenv;
	}

	public static void setDestinationenv(String destinationenv) {
		Params.destinationenv = destinationenv;
	}

	public static String getSourceclientid() {
		return sourceclientid;
	}

	public static void setSourceclientid(String sourceclientid) {
		Params.sourceclientid = sourceclientid;
	}

	public static String getDestinationclientid() {
		return destinationclientid;
	}

	public static void setDestinationclientid(String destinationclientid) {
		Params.destinationclientid = destinationclientid;
	}

	public static String getSourceoidcPolicy() {
		return sourceoidcPolicy;
	}

	public static void setSourceoidcPolicy(String sourceoidcPolicy) {
		Params.sourceoidcPolicy = sourceoidcPolicy;
	}

	public static String getDestinationoidcPolicy() {
		return destinationoidcPolicy;
	}

	public static void setDestinationoidcPolicy(String destinationoidcPolicy) {
		Params.destinationoidcPolicy = destinationoidcPolicy;
	}

	public static String getRegion() {
		return region;
	}

	public static void setRegion(String region) {
		Params.region = region;
	}

}
