create table if not exists "user" (id serial PRIMARY KEY,
					 name VARCHAR(100) NOT null UNIQUE check( name != ''),
					 password VARCHAR(100) not null check( name != ''),
					 created_at timestamp default now());

create table if not exists "post" (id serial PRIMARY KEY,
								   "text" text not null,
								   created_at timestamp default now(),
								   user_id int references "user"(id) not null);

create table if not exists "comment" (id serial PRIMARY KEY,
									  "text" text not null,
								      created_at timestamp default now(),
									  user_id int references "user"(id) not null,
									  post_id int references "post"(id) not null);

create table if not exists "like" (id serial PRIMARY KEY,
									  user_id int references "user"(id) not null,
								      post_id int references "post"(id),
									  comment_id int references "comment"(id),
                                      unique(user_id, post_id, comment_id),
								  	  check (
										  ((coalesce(post_id, 0))::boolean::int + (coalesce(comment_id, 0))::boolean::int) = 1));