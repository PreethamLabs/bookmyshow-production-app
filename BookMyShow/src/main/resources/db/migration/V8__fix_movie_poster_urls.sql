UPDATE movie
SET poster_url = CASE id
    WHEN 17 THEN 'https://upload.wikimedia.org/wikipedia/en/d/d7/RRR_Poster.jpg'
    WHEN 18 THEN 'https://upload.wikimedia.org/wikipedia/en/7/75/Pushpa_-_The_Rise_%282021_film%29.jpg'
    WHEN 19 THEN 'https://upload.wikimedia.org/wikipedia/en/7/75/Leo_%282023_Indian_film%29.jpg'
    WHEN 20 THEN 'https://upload.wikimedia.org/wikipedia/en/c/cb/Jailer_2023_Tamil_film_poster.jpg'
    WHEN 21 THEN 'https://upload.wikimedia.org/wikipedia/en/d/d1/Aavesham.jpg'
    WHEN 22 THEN 'https://upload.wikimedia.org/wikipedia/en/4/4c/Kalki_2898_AD.jpg'
    WHEN 23 THEN 'https://upload.wikimedia.org/wikipedia/en/f/f2/12th_Fail_poster.jpeg'
    WHEN 24 THEN 'https://upload.wikimedia.org/wikipedia/en/a/a1/Stree_2.jpg'
    ELSE poster_url
END
WHERE id BETWEEN 17 AND 24;
