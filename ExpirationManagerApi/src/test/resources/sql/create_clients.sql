DELETE FROM public.clients;

INSERT INTO public.clients(id, birthday, email, first_name, last_name, passport, patronymic_name) VALUES
    (1, '2020-01-06', 'sigma@mmm.mmm', 'Burundi', 'Koch', '6010-000000', ''),
    (2, '2020-01-06', 'sigma@mmm.mumkmk', 'Tom', 'Skier', '0000-000000', ''),
    (3, '2020-01-06', 'theta@google.kukusfgfgf', 'Bork', 'Meister', '1345-000375', ''),
    (4, '2020-01-06', 'upsilon@yahoo.abcdefgtrew', 'Vlad', 'Monster', '0123-000000', 'Torn'),
    (5, '2001-11-06', 'omega@java.sefeftgsc', 'Oleg', 'Turning', '6666-777888', '');

alter sequence clients_seq restart with 55;