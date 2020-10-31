INSERT INTO users
    (id, email, is_moderator, reg_time, name, password)
VALUES
    (1, 'email@yandex.com', 0, STR_TO_DATE('2020-11-11', '%Y-%m-%d'), 'Иван1', 'hashPassword'),
    (2, 'email@yandex.com', 0, STR_TO_DATE('2020-11-11', '%Y-%m-%d'), 'Иван2', 'hashPassword'),
    (3, 'email@yandex.com', 0, STR_TO_DATE('2020-11-11', '%Y-%m-%d'), 'Иван3', 'hashPassword');

INSERT INTO posts
    (id, is_active, user_id, time, title, text, view_count)
VALUES
    (1,0, 1, STR_TO_DATE('20201111 1130','%Y%m%d %h%i'), 'тестовый пост 1', 'текст поста №1', 0),
    (2,1, 1, STR_TO_DATE('20201111 1130','%Y%m%d %h%i'), 'тестовый пост 2', 'текст поста №2', 0),
    (3,0, 2, STR_TO_DATE('20201111 1130','%Y%m%d %h%i'), 'тестовый пост 3', 'текст поста №3', 0);

INSERT INTO post_votes
    (id, user_id, post_id, time, value)
VALUES
    (1, 1, 2, STR_TO_DATE('20201111 1140','%Y%m%d %h%i'), 1),
    (2, 1, 2, STR_TO_DATE('20201111 1140','%Y%m%d %h%i'), 1),
    (3, 1, 2, STR_TO_DATE('20201111 1140','%Y%m%d %h%i'), 1);

INSERT INTO tags
    (id, name)
VALUES
    (1, 'тег1'),
    (2, 'тег2');

INSERT INTO tag2post
    (id, post_id, tag_id)
VALUES
    (1, 1, 1),
    (2, 1, 2);

INSERT INTO post_comments
    (id, post_id, user_id, time, text)
VALUES
    (1, 1, 1, STR_TO_DATE('20201111 1150','%Y%m%d %h%i'), 'текс коммента 1'),
    (2, 2, 2, STR_TO_DATE('20201111 1150','%Y%m%d %h%i'), 'текс коммента 2');
