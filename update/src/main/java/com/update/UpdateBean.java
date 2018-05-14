package com.update;

/**
 * Created by vange on 2017/12/15.
 */

public class UpdateBean {

    /**
     * data : {"appSize":"16.1M","downloadLink":"http://120.35.30.201/gjj/upload/20171027/11e92855-1bbf-42df-8d16-5a45b27651e2/app-release_10_jiagu_sign.apk","updateInformation":"1、更新版本检查功能","updateNumber":"2","versionNumber":"1.2"}
     * status : 1
     */

        /**
         * appSize : 16.1M
         * downloadLink : http://120.35.30.201/gjj/upload/20171027/11e92855-1bbf-42df-8d16-5a45b27651e2/app-release_10_jiagu_sign.apk
         * updateInformation : 1、更新版本检查功能
         * updateNumber : 2
         * versionNumber : 1.2
         */

        private String appSize;
        private String downloadLink;
        private String updateInformation;
        private String updateNumber;
        private String versionNumber;

        public String getAppSize() {
            return appSize;
        }

        public void setAppSize(String appSize) {
            this.appSize = appSize;
        }

        public String getDownloadLink() {
            return downloadLink;
        }

        public void setDownloadLink(String downloadLink) {
            this.downloadLink = downloadLink;
        }

        public String getUpdateInformation() {
            return updateInformation;
        }

        public void setUpdateInformation(String updateInformation) {
            this.updateInformation = updateInformation;
        }

        public String getUpdateNumber() {
            return updateNumber;
        }

        public void setUpdateNumber(String updateNumber) {
            this.updateNumber = updateNumber;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(String versionNumber) {
            this.versionNumber = versionNumber;
        }
}
