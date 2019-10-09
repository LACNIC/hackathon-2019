#bgpq3

La herramienta que les comente hoy que se puede utilizar para crear filtros en función del atributo route de los registros de los IRR se llama bgpq3.

A modo de ejemplo

Para Juniper


$ bgpq3 -J6l LACNIC AS28000

policy-options {

replace:

 prefix-list LACNIC {

    2001:67c:1010::/48;

    2001:67c:1011::/48;

    2001:7fd::/48;

    2001:7fe::/33;

    2001:13c7:7001::/48;

    2001:13c7:7002::/48;

    2001:13c7:7003::/48;

    2001:13c7:7010::/46;

    2001:13c7:7020::/44;

    2800:1b8::/46;

    2801::/48;

    2801:14:a000::/48;

    2801:1b8::/44;

    2801:1b8:4::/46;

 }

}

Para Cisco


$ bgpq3 -6l LACNIC AS28000

no ipv6 prefix-list LACNIC

ipv6 prefix-list LACNIC permit 2001:67c:1010::/48

ipv6 prefix-list LACNIC permit 2001:67c:1011::/48

ipv6 prefix-list LACNIC permit 2001:7fd::/48

ipv6 prefix-list LACNIC permit 2001:7fe::/33

ipv6 prefix-list LACNIC permit 2001:13c7:7001::/48

ipv6 prefix-list LACNIC permit 2001:13c7:7002::/48

ipv6 prefix-list LACNIC permit 2001:13c7:7003::/48

ipv6 prefix-list LACNIC permit 2001:13c7:7010::/46

ipv6 prefix-list LACNIC permit 2001:13c7:7020::/44

ipv6 prefix-list LACNIC permit 2800:1b8::/46

ipv6 prefix-list LACNIC permit 2801::/48

ipv6 prefix-list LACNIC permit 2801:14:a000::/48

ipv6 prefix-list LACNIC permit 2801:1b8::/44

ipv6 prefix-list LACNIC permit 2801:1b8:4::/46

Saludos
José Restaino