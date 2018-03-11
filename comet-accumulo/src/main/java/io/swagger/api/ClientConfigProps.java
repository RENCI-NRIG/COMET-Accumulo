package io.swagger.api;

public class ClientConfigProps {
	
	/*CLIENT CONFIGURATION INFORMATION*/
	public static String COMET_CLIENT_KEYSTORE_PROP = "comet.client.keystore";
	public static String COMET_CLIENT_TRUSTTORE_PROP = "comet.client.truststore";
	public static String COMET_CLIENT_KEYSTORE_PASS_PROP =  "comet.client.keystore.pass";
	public static String COMET_CLIENT_TRUSTSTORE_PASS_PROP = "comet.client.truststore.pass";
	public static String COMET_CLIENT_ACCUMULO_INSTANCE_PROP = "comet.client.accumulo.instance";
	public static String COMET_CLIENT_ZOOKEEPERS_HOSTS_PROP = "comet.client.zookeeperse.hosts";
	public static String COMET_CLIENT_ACCUMULO_MAINTABLE_PROP = "comet.client.table.main";
	public static String COMET_CLIENT_ACCUMULO_USER_DEFAULT = "comet.client.accumulo.user.default";
	public static String COMET_CLIENT_ACCUMULO_USER_DEFAULT_PASS = "comet.client.accumulo.user.default.pass";
	public static String COMET_CLIENT_ACCUMULO_IFCETABLE_PROP = "comet.client.table.ifce";
	public static String COMET_CLIENT_ACCUMULO_MEMBUFF = "comet.client.accumulo.membuff";
	public static String COMET_CLIENT_ACCUMULO_NUMTHREADS = "comet.client.accumulo.numthreads";
	public static String COMET_CLIENT_ACCUMULO_KEYSTORE_TYPE="comet.client.keystore.type";
	public static String COMET_CLIENT_ACCUMULO_TRUSTSTORE_TYPE="comet.client.truststore.type";
	
	public static String COMET_CLIENT_ACCUMULO_LABEL_ACTOR = "comet.service.auth.label.actor";
	public static String COMET_CLIENT_ACCUMULO_LABEL_USER = "comet.service.auth.label.user";
	
	/*SERVICE CONFIGURATION INFORMATION*/
	public static String COMET_SERVICE_ACCUMULO_TABLE_SLICES = "comet.service.accumulo.table.slices";
	public static String COMET_SERVICE_ACCUMULO_TABLE_RESERVATIONS =  "comet.service.accumulo.table.reservations";
	public static String COMET_SERVICE_ACCUMULO_TABLE_PRINCIPALS = "comet.service.accumulo.table.principals";
	public static String COMET_SERVICE_ACCUMULO_AUTH_ROOTPASSWORD = "comet.service.auth.root.password";
	public static String COMET_SERVICE_ACCUMULO_AUTH_ROOT= "comet.service.auth.root";
	public static String COMET_SERVICE_ACCUMULO_AUTH_LABEL_USER="comet.service.auth.label.user";
	public static String COMET_SERVICE_ACCUMULO_AUTH_LABEL_ACTOR="comet.service.auth.label.actor";
	public static String COMET_SERVICE_ACCUMULO_DATASCHEMA_IAAS="comet.service.dataschema.iaas";
	public static String COMET_SERVICE_ACCUMULO_DATASCHEMA_USER="comet.service.dataschema.user";

}
