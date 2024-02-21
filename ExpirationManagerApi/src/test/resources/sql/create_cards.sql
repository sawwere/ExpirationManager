DELETE FROM public.cards;

INSERT INTO public.cards(id, card_number, date_of_expiration,date_of_issue,status,client_id) VALUES
    (1, '111100351', '2024-11-24', '2020-11-11', 'OK', 2),
    (2, '111100345', '2024-10-11', '2014-06-06', 'OK', 2),
    (3, '4256100345', '2024-10-11', '2014-08-06', 'OK', 2),
    (4, '4256134528', '2024-10-11', '2024-08-06', 'OK', 1),
    (5, '4256130008', '2024-10-11', '2020-08-06', 'OK', 1);

alter sequence cards_seq restart with 55;