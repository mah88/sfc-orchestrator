package org.project.sfc.com.OpenBaton.Messages;

/**
 * Created by mah on 3/1/16.
 */
public class SfcOpenbatonMessage {


        public String info;
        public String message;

        public SfcOpenbatonMessage() {
        }

        public SfcOpenbatonMessage(String info, String message) {
            this.info = info;
            this.message = message;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

}
