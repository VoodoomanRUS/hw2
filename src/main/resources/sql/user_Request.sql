select
u.name as "Пользователь",
u.created_at as "Дата создания",
(select "text" from "post" where id = u.id order by created_at limit 1) as "самый первый пост",
(select count(*) from "comment" where user_id = u.id) as "Количество комментариев"
from "user" as u
where id = %d;
