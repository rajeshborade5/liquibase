INSERT INTO tradable (id, name, amt)
SELECT g.id, 'trade' || g.id, g.id
FROM generate_series(1, 500000) AS g (id) ;

INSERT INTO quote (id, quote)
SELECT g.id, 'quote' || g.id
FROM generate_series(1, 500000) AS g (id) ;

INSERT INTO order_
SELECT g.id, 'order_' || g.id
FROM generate_series(1, 500000) AS g (id) ;