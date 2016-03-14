package org.project.sfc.com.ODL_SFC_driver.ODL_SFC;

/**
 * Created by mah on 2/8/16.
 */
public class VNFdict {



        private String neutronPortId;
        private String ip;
        private String type;
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getNeutronPortId() {
            return neutronPortId;
        }
        public void setNeutronPortId(String ID) {
            this.neutronPortId = ID;
        }
        public String getIP() {
            return ip;
        }
        public void setIP(String ip) {
            this.ip = ip;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }



}
