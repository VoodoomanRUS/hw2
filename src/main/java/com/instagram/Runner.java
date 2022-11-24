package com.instagram;

public class Runner {
    public static void main(String[] args) {
        try (InstDAO instDAO = new InstDAO()) {
            instDAO.dropTables();
            instDAO.createTables();
            instDAO.fillData();
//            instDAO.addNewUser("dima", "1234");
//            instDAO.addNewPost("abracadabra,bro", 1);
//            instDAO.addNewComment("wtoblet", 1, 1);
//            instDAO.addNewLike(null, 1, 1);

            System.out.println(instDAO.statRequest());
            System.out.println(instDAO.getUserInfo(1));

//            instDAO.addNewLike(1, null, 1);

        }

    }

}