SELECT
(SELECT count(id) FROM "user") as "количество пользователей" ,
(SELECT count(id) FROM "post") as "количество постов" ,
(SELECT count(id) FROM "comment") as "количество комментариев" ,
(SELECT count(id) FROM "like") as "количество лайков"

