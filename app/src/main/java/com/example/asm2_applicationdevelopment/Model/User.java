package com.example.asm2_applicationdevelopment.Model;

public class User {
        public int id;
        public String username;
        public String password;
        public String email;
        public String phone;
        private String profileImagePath; // Thêm trường này

        public void setId(int id) {
            this.id = id;
        }
        public void setUsername(String username) {
            this.username = username;
        }
    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

    }


