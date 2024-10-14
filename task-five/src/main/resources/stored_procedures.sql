CREATE OR REPLACE FUNCTION get_post_likes_report()
RETURNS TABLE(
    post_id INT,
    post_text TEXT,
    author_name VARCHAR,
    author_surname VARCHAR,
    total_likes BIGINT,
    post_timestamp TIMESTAMP
)
AS $$
BEGIN
    RETURN QUERY
    SELECT
        p.id AS post_id,
        p.text AS post_text,
        u.name AS author_name,
        u.surname AS author_surname,
        COUNT(l.userid) AS total_likes,
        p.timestamp AS post_timestamp
    FROM
        posts p
    JOIN
        users u ON p.userid = u.id
    LEFT JOIN
        likes l ON p.id = l.postid
    GROUP BY
        p.id, p.text, u.name, u.surname, p.timestamp
    HAVING
        COUNT(l.userid) > 0
    ORDER BY
        total_likes DESC,
        p.timestamp DESC;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_user_activity_report()
RETURNS TABLE(
    user_id INT,
    name VARCHAR,
    surname VARCHAR,
    total_friends BIGINT,
    total_posts BIGINT
)
AS $$
BEGIN
    RETURN QUERY
    SELECT
        u.id AS user_id,
        u.name,
        u.surname,
        COUNT(DISTINCT f.userid2) AS total_friends,
        COUNT(DISTINCT p.id) AS total_posts
    FROM
        users u
    LEFT JOIN
        friendships f ON u.id = f.userid1
    LEFT JOIN
        posts p ON u.id = p.userid
    GROUP BY
        u.id, u.name, u.surname
    ORDER BY
        total_friends DESC,
        total_posts DESC;
END;
$$ LANGUAGE plpgsql;