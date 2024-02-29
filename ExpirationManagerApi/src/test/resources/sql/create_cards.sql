DELETE FROM public.cards;

INSERT INTO public.cards(id, card_number, date_of_expiration,date_of_issue,status,client_id) VALUES
    (1, '9879671001709031', '2024-11-24', '2020-11-11', 'OK', 2),
    (2, '1788513001709031', '2024-10-11', '2014-06-06', 'OK', 2),
    (3, '9959659001709030', '2024-10-11', '2014-08-06', 'OK', 2),
    (4, '1708536001709039', '2024-10-11', '2024-08-06', 'OK', 1),
    (5, '4970200170903436', '2024-10-11', '2020-08-06', 'OK', 1);

alter sequence cards_seq restart with 55;