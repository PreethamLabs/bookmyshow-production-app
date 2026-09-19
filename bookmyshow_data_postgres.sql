-- =========================================================
-- BOOKMYSHOW - LARGE REALISTIC SEED DATA
-- Database: bookmyshow
--
-- 12 Cities
-- 24 Movies
-- 24 Theatres (2 per city)
-- 48 Screens (2 per theatre)
-- 60 Seats per screen
--     30 REGULAR
--     20 PREMIER
--     10 RECLINER
-- 384 Shows
-- 23,040 Show Seats
--
-- USERS ARE NOT TOUCHED
-- BOOKINGS / PAYMENTS ARE NOT TOUCHED
-- =========================================================

BEGIN;

-- =========================================================
-- 0. CLEAN OLD CATALOG DATA
-- =========================================================

-- DELETE FROM booking_seat;
-- DELETE FROM ticket;
-- DELETE FROM booking;
-- DELETE FROM show_seat;
-- DELETE FROM "show";
-- DELETE FROM seat;
-- DELETE FROM screen;
-- DELETE FROM theatre;
-- DELETE FROM movie_person;
-- DELETE FROM movie_genre;
-- DELETE FROM movie_language;
-- DELETE FROM person;
-- DELETE FROM genre;
-- DELETE FROM language;
-- DELETE FROM movie;
-- DELETE FROM city;


-- =========================================================
-- 1. CITIES
-- =========================================================

INSERT INTO city (id, name, state, country) VALUES
(1,  'Bengaluru',    'Karnataka',       'India'),
(2,  'Mumbai',       'Maharashtra',     'India'),
(3,  'Hyderabad',    'Telangana',       'India'),
(4,  'Chennai',      'Tamil Nadu',      'India'),
(5,  'Delhi',        'Delhi',           'India'),
(6,  'Pune',         'Maharashtra',     'India'),
(7,  'Kolkata',      'West Bengal',     'India'),
(8,  'Ahmedabad',    'Gujarat',         'India'),
(9,  'Kochi',        'Kerala',          'India'),
(10, 'Jaipur',       'Rajasthan',       'India'),
(11, 'Lucknow',      'Uttar Pradesh',   'India'),
(12, 'Chandigarh',   'Chandigarh',      'India');


-- =========================================================
-- 2. GENRES
-- =========================================================

INSERT INTO genre (id, name) VALUES
(1, 'Action'),
(2, 'Adventure'),
(3, 'Drama'),
(4, 'Science Fiction'),
(5, 'Thriller'),
(6, 'Crime'),
(7, 'Comedy'),
(8, 'Romance'),
(9, 'Animation'),
(10, 'Fantasy'),
(11, 'Horror'),
(12, 'Biography');


-- =========================================================
-- 3. LANGUAGES
-- =========================================================

INSERT INTO language (id, name) VALUES
(1, 'English'),
(2, 'Hindi'),
(3, 'Kannada'),
(4, 'Telugu'),
(5, 'Tamil'),
(6, 'Malayalam'),
(7, 'Bengali'),
(8, 'Gujarati'),
(9, 'Punjabi');


-- =========================================================
-- 4. PEOPLE
-- =========================================================

INSERT INTO person (id, name) VALUES
(1,  'Christopher Nolan'),
(2,  'Matthew McConaughey'),
(3,  'Anne Hathaway'),
(4,  'Leonardo DiCaprio'),
(5,  'Joseph Gordon-Levitt'),
(6,  'Cillian Murphy'),
(7,  'Emily Blunt'),
(8,  'Robert Downey Jr.'),
(9,  'Denis Villeneuve'),
(10, 'Timothée Chalamet'),
(11, 'Zendaya'),
(12, 'Christian Bale'),
(13, 'Heath Ledger'),
(14, 'Tom Hardy'),
(15, 'Ryan Gosling'),
(16, 'Margot Robbie'),
(17, 'Brad Pitt'),
(18, 'Keanu Reeves'),
(19, 'Tom Cruise'),
(20, 'Emma Stone'),
(21, 'Rajkummar Rao'),
(22, 'Alia Bhatt'),
(23, 'Ranbir Kapoor'),
(24, 'Yash'),
(25, 'Rishab Shetty'),
(26, 'Prabhas'),
(27, 'Allu Arjun'),
(28, 'Jr NTR'),
(29, 'Ram Charan'),
(30, 'Dhanush'),
(31, 'Vijay'),
(32, 'Suriya'),
(33, 'Fahadh Faasil'),
(34, 'Dulquer Salmaan'),
(35, 'Mohanlal'),
(36, 'Satyadev');



-- =========================================================
-- 5. MOVIES
-- =========================================================

INSERT INTO movie
(id, title, description, poster_url, movie_status, duration_in_minutes, release_date)
VALUES

(1,
 'Interstellar',
 'A team of explorers travels through a wormhole in space in search of a new home for humanity.',
 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg',
 'RELEASED', 169, '2014-11-07'),

(2,
 'Inception',
 'A skilled extractor who enters the dreams of others is given a final impossible mission.',
 'https://image.tmdb.org/t/p/w500/oYuLEt3zVCKq57qu2F8dT7NIa6f.jpg',
 'RELEASED', 148, '2010-07-16'),

(3,
 'Oppenheimer',
 'The story of J. Robert Oppenheimer and the development of the first atomic bomb.',
 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg',
 'RELEASED', 180, '2023-07-21'),

(4,
 'Dune: Part Two',
 'Paul Atreides joins the Fremen while seeking revenge against those who destroyed his family.',
 'https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg',
 'RELEASED', 166, '2024-03-01'),

(5,
 'The Dark Knight',
 'Batman faces the Joker, a criminal mastermind who pushes Gotham into chaos.',
 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
 'RELEASED', 152, '2008-07-18'),

(6,
 'The Batman',
 'Batman investigates a series of crimes that reveal a dark conspiracy within Gotham.',
 'https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg',
 'RELEASED', 176, '2022-03-04'),

(7,
 'Top Gun: Maverick',
 'A veteran pilot returns to train a new generation of elite fighter pilots.',
 'https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg',
 'RELEASED', 131, '2022-05-27'),

(8,
 'Avatar: The Way of Water',
 'The Sully family explores the oceans of Pandora while protecting their home.',
 'https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg',
 'RELEASED', 192, '2022-12-16'),

(9,
 'Spider-Man: No Way Home',
 'Peter Parker seeks help after his identity is exposed and accidentally opens the multiverse.',
 'https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg',
 'RELEASED', 148, '2021-12-17'),

(10,
 'Avengers: Endgame',
 'The Avengers attempt one final mission to reverse the devastating consequences of Thanos.',
 'https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg',
 'RELEASED', 181, '2019-04-26'),

(11,
 'John Wick: Chapter 4',
 'John Wick battles his way through a global criminal organization to earn his freedom.',
 'https://image.tmdb.org/t/p/w500/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg',
 'RELEASED', 169, '2023-03-24'),

(12,
 'Mission: Impossible - Dead Reckoning',
 'Ethan Hunt faces a powerful artificial intelligence threat capable of controlling global systems.',
 'https://image.tmdb.org/t/p/w500/NNxYkU70HPurnNCSiCjYAmacwm.jpg',
 'RELEASED', 163, '2023-07-12'),

(13,
 'Dhurandhar',
 'An undercover Indian operative infiltrates Karachi''s criminal underworld and rises through its ranks to dismantle a dangerous network from within.',
 'https://m.media-amazon.com/images/M/MV5BMzFiNTVkZjYtM2I3Yi00MGNjLWEyYTAtMGViNGExZmMzMGMzXkEyXkFqcGc@._V1_.jpg',
 'RELEASED', 214, '2025-12-05'),

(14,
 'Joker',
 'A troubled man descends into a dark transformation that changes Gotham forever.',
 'https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg',
 'RELEASED', 122, '2019-10-04'),

(15,
 'KGF: Chapter 2',
 'Rocky rises to power and confronts enemies determined to destroy his empire.',
 'https://image.tmdb.org/t/p/w500/khNVygolU0TxLIDWff5tQlAhZ23.jpg',
 'RELEASED', 168, '2022-04-14'),

(16,
 'Kantara',
 'A village legend and a mysterious force shape the destiny of a man protecting his ancestral land.',
 'https://upload.wikimedia.org/wikipedia/en/8/84/Kantara_poster.jpeg',
 'RELEASED', 148, '2022-09-30'),

(17,
 'RRR',
 'Two revolutionaries from different backgrounds form an extraordinary friendship while fighting colonial rule.',
 'https://upload.wikimedia.org/wikipedia/en/d/d7/RRR_Poster.jpg',
 'RELEASED', 182, '2022-03-25'),

(18,
 'Pushpa: The Rise',
 'A determined labourer rises through the dangerous world of red sandalwood smuggling.',
 'https://upload.wikimedia.org/wikipedia/en/7/75/Pushpa_-_The_Rise_%282021_film%29.jpg',
 'RELEASED', 179, '2021-12-17'),

(19,
 'Leo',
 'A quiet cafe owner is forced to confront a violent past when dangerous people enter his life.',
 'https://upload.wikimedia.org/wikipedia/en/7/75/Leo_%282023_Indian_film%29.jpg',
 'RELEASED', 164, '2023-10-19'),

(20,
 'Jailer',
 'A retired jailer returns to action when his family becomes involved with a dangerous criminal network.',
 'https://upload.wikimedia.org/wikipedia/en/c/cb/Jailer_2023_Tamil_film_poster.jpg',
 'RELEASED', 168, '2023-08-10'),

(21,
 'Aavesham',
 'Three college students get involved with a charismatic local gangster and quickly lose control of the situation.',
 'https://upload.wikimedia.org/wikipedia/en/d/d1/Aavesham.jpg',
 'RELEASED', 158, '2024-04-11'),

(22,
 'Kalki 2898 AD',
 'In a distant future, humanity faces an extraordinary battle surrounding a mysterious child and an ancient prophecy.',
 'https://upload.wikimedia.org/wikipedia/en/4/4c/Kalki_2898_AD.jpg',
 'RELEASED', 181, '2024-06-27'),

(23,
 '12th Fail',
 'A young man overcomes poverty and repeated setbacks while pursuing his dream of becoming a civil servant.',
 'https://upload.wikimedia.org/wikipedia/en/f/f2/12th_Fail_poster.jpeg',
 'RELEASED', 147, '2023-10-27'),

(24,
 'Stree 2',
 'A mysterious supernatural force returns to a small town and a group of friends must face it again.',
 'https://upload.wikimedia.org/wikipedia/en/a/a1/Stree_2.jpg',
 'RELEASED', 149, '2024-08-15');


-- =========================================================
-- 6. MOVIE GENRES
-- =========================================================

INSERT INTO movie_genre (movie_id, genre_id) VALUES
(1,4),(1,2),(1,3),
(2,5),(2,4),(2,1),
(3,3),(3,12),
(4,4),(4,2),(4,1),
(5,1),(5,5),(5,6),
(6,1),(6,5),(6,6),
(7,1),(7,2),(7,3),
(8,4),(8,2),(8,1),
(9,1),(9,2),(9,10),
(10,1),(10,2),(10,4),
(11,1),(11,5),(11,6),
(12,1),(12,5),(12,2),
(13,7),(13,8),
(14,3),(14,5),(14,6),
(15,1),(15,2),(15,3),
(16,1),(16,10),(16,3),
(17,1),(17,2),(17,3),
(18,1),(18,6),(18,3),
(19,1),(19,5),(19,3),
(20,1),(20,5),(20,6),
(21,7),(21,1),(21,3),
(22,4),(22,2),(22,1),
(23,3),(23,12),
(24,7),(24,11),(24,3);


-- =========================================================
-- 7. MOVIE LANGUAGES
-- =========================================================

INSERT INTO movie_language (movie_id, language_id) VALUES
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1),
(7,1),
(8,1),
(9,1),
(10,1),
(11,1),
(12,1),
(13,1),
(14,1),
(15,2),(15,3),
(16,3),
(17,2),(17,4),(17,1),
(18,2),(18,4),
(19,5),
(20,5),
(21,6),
(22,4),(22,2),(22,1),
(23,2),
(24,2);


-- =========================================================
-- 8. MOVIE PEOPLE
-- =========================================================

INSERT INTO movie_person (movie_id, person_id, role) VALUES
(1,1,'DIRECTOR'),(1,2,'ACTOR'),(1,3,'ACTOR'),
(2,1,'DIRECTOR'),(2,4,'ACTOR'),(2,5,'ACTOR'),
(3,1,'DIRECTOR'),(3,6,'ACTOR'),(3,7,'ACTOR'),(3,8,'ACTOR'),
(4,9,'DIRECTOR'),(4,10,'ACTOR'),(4,11,'ACTOR'),
(5,12,'ACTOR'),(5,13,'ACTOR'),(5,14,'ACTOR'),
(6,12,'ACTOR'),
(7,19,'ACTOR'),
(8,20,'ACTOR'),
(9,18,'ACTOR'),
(10,8,'ACTOR'),
(11,18,'ACTOR'),
(12,19,'ACTOR'),
(13,16,'ACTOR'),(13,20,'ACTOR'),
(14,16,'ACTOR'),
(15,24,'ACTOR'),(15,25,'DIRECTOR'),
(16,25,'ACTOR'),(16,25,'DIRECTOR'),
(17,28,'ACTOR'),(17,29,'ACTOR'),
(18,27,'ACTOR'),
(19,31,'ACTOR'),
(20,31,'ACTOR'),
(21,33,'ACTOR'),
(22,26,'ACTOR'),
(23,21,'ACTOR'),
(24,22,'ACTOR');


-- =========================================================
-- 9. THEATRES
-- 2 THEATRES PER CITY
-- =========================================================

INSERT INTO theatre (id, name, address, city_id) VALUES

-- Bengaluru
(1,  'PVR Orion Mall',              'Brigade Gateway, Dr Rajkumar Road', 1),
(2,  'INOX Garuda Mall',            'Magrath Road, Ashok Nagar', 1),

-- Mumbai
(3,  'PVR Phoenix Marketcity',      'Kurla West, Mumbai', 2),
(4,  'INOX R-City',                 'Ghatkopar West, Mumbai', 2),

-- Hyderabad
(5,  'AMB Cinemas',                 'Gachibowli, Hyderabad', 3),
(6,  'PVR Irrum Manzil',            'Khairatabad, Hyderabad', 3),

-- Chennai
(7,  'PVR VR Chennai',              'Anna Nagar, Chennai', 4),
(8,  'INOX The Marina Mall',        'OMR, Chennai', 4),

-- Delhi
(9,  'PVR Select Citywalk',         'Saket, New Delhi', 5),
(10, 'INOX Nehru Place',            'Nehru Place, New Delhi', 5),

-- Pune
(11, 'PVR Pavilion Mall',            'Senapati Bapat Road, Pune', 6),
(12, 'INOX Amanora Mall',            'Hadapsar, Pune', 6),

-- Kolkata
(13, 'INOX South City',              'Prince Anwar Shah Road, Kolkata', 7),
(14, 'PVR Mani Square',              'Kankurgachi, Kolkata', 7),

-- Ahmedabad
(15, 'PVR Acropolis',                'Thaltej, Ahmedabad', 8),
(16, 'INOX Himalaya Mall',           'Drive-In Road, Ahmedabad', 8),

-- Kochi
(17, 'PVR Lulu Mall',                'Edappally, Kochi', 9),
(18, 'Cinepolis Centre Square',      'MG Road, Kochi', 9),

-- Jaipur
(19, 'INOX GT Central',              'Malviya Nagar, Jaipur', 10),
(20, 'PVR World Trade Park',         'Malviya Nagar, Jaipur', 10),

-- Lucknow
(21, 'PVR Phoenix United',           'Alambagh, Lucknow', 11),
(22, 'INOX Phoenix Palassio',        'Gomti Nagar, Lucknow', 11),

-- Chandigarh
(23, 'PVR Elante Mall',              'Industrial Area, Chandigarh', 12),
(24, 'INOX Bestech Square',          'Sector 66, Chandigarh', 12);


-- =========================================================
-- 10. SCREENS
-- 2 SCREENS PER THEATRE
-- =========================================================

INSERT INTO screen (id, name, theatre_id)
SELECT
    ((t.id - 1) * 2) + 1,
    'Screen 1',
    t.id
FROM theatre t

UNION ALL

SELECT
    ((t.id - 1) * 2) + 2,
    'Screen 2',
    t.id
FROM theatre t;


-- =========================================================
-- 11. SEATS
--
-- 60 SEATS PER SCREEN
--
-- A1-A30  = REGULAR
-- B1-B20  = PREMIER
-- C1-C10  = RECLINER
--
-- ONLY THESE THREE SEAT TYPES
-- =========================================================

INSERT INTO seat (id, seat_number, seat_type, screen_id)
SELECT
    ((screen_id - 1) * 60) + seat_number,
    CASE
        WHEN seat_number <= 30
            THEN 'A' || seat_number
        WHEN seat_number <= 50
            THEN 'B' || (seat_number - 30)
        ELSE
            'C' || (seat_number - 50)
    END,
    CASE
        WHEN seat_number <= 30
            THEN 'REGULAR'
        WHEN seat_number <= 50
            THEN 'PREMIER'
        ELSE
            'RECLINER'
    END,
    screen_id
FROM (
    SELECT
        s.id AS screen_id,
        generate_series(1,60) AS seat_number
    FROM screen s
) x;


-- =========================================================
-- 12. SHOWS
--
-- 8 SHOWS PER SCREEN
-- 48 SCREENS
-- = 384 SHOWS
--
-- DATES:
-- 2026-09-16 through 2026-09-23
--
-- Every screen has:
-- 10:00
-- 13:00
-- 16:00
-- 19:00
-- plus additional evening/morning rotations
-- =========================================================

INSERT INTO "show"
(
    id,
    movie_id,
    screen_id,
    show_date,
    ticket_price,
    start_time,
    end_time
)
SELECT
    ROW_NUMBER() OVER (ORDER BY s.id, d.show_date, times.start_time),

    (
        ((s.id - 1) % 24) + 1
    ) AS movie_id,

    s.id AS screen_id,

    d.show_date,

    CASE
        WHEN times.start_time IN ('10:00','13:00') THEN
            220 + ((s.id % 5) * 10)
        WHEN times.start_time IN ('16:00','19:00') THEN
            280 + ((s.id % 5) * 15)
        ELSE
            250 + ((s.id % 5) * 10)
    END AS ticket_price,

    times.start_time,

    (
        times.start_time
        +
        (
            CASE
                WHEN (((s.id - 1) % 24) + 1) IN (1,2,3,4,5,6,7,8,9,10,11,12)
                    THEN INTERVAL '2 hours 50 minutes'
                ELSE INTERVAL '2 hours 40 minutes'
            END
        )
    )::time AS end_time

FROM screen s

CROSS JOIN (
    SELECT generate_series(
        DATE '2026-09-16',
        DATE '2026-09-23',
        INTERVAL '1 day'
    )::date AS show_date
) d

CROSS JOIN (
    VALUES
        (TIME '10:00'),
        (TIME '13:00'),
        (TIME '16:00'),
        (TIME '19:00')
) AS times(start_time)

WHERE
    -- 4 shows per screen per day
    s.id <= 48;


-- =========================================================
-- 13. SHOW SEATS
--
-- EVERY SHOW GETS EVERY SEAT OF ITS SCREEN
--
-- 384 SHOWS × 60 SEATS
-- = 23,040 SHOW SEATS
--
-- ALL AVAILABLE
-- =========================================================

INSERT INTO show_seat
(
    id,
    show_id,
    seat_id,
    seat_status,
    locked_until
)
SELECT
    ROW_NUMBER() OVER (
        ORDER BY sh.id, s.id
    ) AS id,

    sh.id,

    s.id,

    'AVAILABLE',

    NULL

FROM "show" sh

JOIN seat s
    ON s.screen_id = sh.screen_id;


-- =========================================================
-- 14. RESET SEQUENCES
-- =========================================================

SELECT setval(
    pg_get_serial_sequence('city', 'id'),
    COALESCE((SELECT MAX(id) FROM city), 1)
);

SELECT setval(
    pg_get_serial_sequence('genre', 'id'),
    COALESCE((SELECT MAX(id) FROM genre), 1)
);

SELECT setval(
    pg_get_serial_sequence('language', 'id'),
    COALESCE((SELECT MAX(id) FROM language), 1)
);

SELECT setval(
    pg_get_serial_sequence('person', 'id'),
    COALESCE((SELECT MAX(id) FROM person), 1)
);

SELECT setval(
    pg_get_serial_sequence('movie', 'id'),
    COALESCE((SELECT MAX(id) FROM movie), 1)
);

SELECT setval(
    pg_get_serial_sequence('theatre', 'id'),
    COALESCE((SELECT MAX(id) FROM theatre), 1)
);

SELECT setval(
    pg_get_serial_sequence('screen', 'id'),
    COALESCE((SELECT MAX(id) FROM screen), 1)
);

SELECT setval(
    pg_get_serial_sequence('seat', 'id'),
    COALESCE((SELECT MAX(id) FROM seat), 1)
);

SELECT setval(
    pg_get_serial_sequence('show', 'id'),
    COALESCE((SELECT MAX(id) FROM "show"), 1)
);

SELECT setval(
    pg_get_serial_sequence('show_seat', 'id'),
    COALESCE((SELECT MAX(id) FROM show_seat), 1)
	);


COMMIT;


-- =========================================================
-- 15. VERIFY DATA
-- =========================================================

SELECT 'cities' AS table_name, COUNT(*) AS total FROM city
UNION ALL
SELECT 'movies', COUNT(*) FROM movie
UNION ALL
SELECT 'theatres', COUNT(*) FROM theatre
UNION ALL
SELECT 'screens', COUNT(*) FROM screen
UNION ALL
SELECT 'seats', COUNT(*) FROM seat
UNION ALL
SELECT 'shows', COUNT(*) FROM "show"
UNION ALL
SELECT 'show_seats', COUNT(*) FROM show_seat;


-- =========================================================
-- EXPECTED
--
-- cities       = 12
-- movies       = 24
-- theatres     = 24
-- screens      = 48
-- seats        = 2880
-- shows        = 1536
-- show_seats   = 92160
--
-- IMPORTANT:
-- 12 cities × 2 theatres × 2 screens ×
-- 8 days × 4 shows/day
-- = 1536 shows
--
-- 1536 × 60 seats
-- = 92160 show seats
-- =========================================================