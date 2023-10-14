package com.daedal00.app.resources;

import java.util.List;

import lombok.Data;

@Data
public class InfoResource {

    public static class InfoResponse {
        private List<String> accounts;
        private String accessToken;
        private String itemId;

        public InfoResponse(List<String> accounts, String accessToken, String itemId) {
            this.accounts = accounts;
            this.accessToken = accessToken;
            this.itemId = itemId;
        }

        public List<String> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<String> accounts) {
            this.accounts = accounts;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }
    }
}
