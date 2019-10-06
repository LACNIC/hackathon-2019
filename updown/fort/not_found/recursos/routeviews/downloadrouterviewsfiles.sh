#!/bin/bash

days=(23 18 06 05 11 17 13 08 09 25 01 12)
months=(06 07 08 09 10 11 12 01 02 03 04 05)
years=(2018 2018 2018 2018 2018 2018 2018 2019 2019 2019 2019 2019)
collectors=( '' 'route-views3' 'route-views4' 'route-views6' 'route-views.eqix' 'route-views.isc' 'route-views.kixp' 'route-views.jinx' 'route-views.linx' 'route-views.telxatl' 'route-views.wide' 'route-views.sydney' 'route-views.saopaulo' 'route-views2.saopaulo' 'route-views.nwax' 'route-views.perth' 'route-views.sg' 'route-views.sfmix' 'route-views.soxrs' 'route-views.chicago' 'route-views.napafrica' 'route-views.flix' 'route-views.chile' 'route-views.amsix' 'route-views.mwix' 'oix-route-views' )

for (( i=0; i<=11; i++ ))
do 
    for col in "${collectors[@]}"
	do
		DAY=${days[$i]}
		MONTH=${months[$i]}
		YEAR=${years[$i]}
		FOLDER=$YEAR.$MONTH
		DATE=$YEAR$MONTH$DAY
		COLLECTOR=${col}

		if [ "$COLLECTOR" == "oix-route-views" ];
		then
		    	wget --execute="robots = off" --mirror --no-parent -c -N  http://archive.routeviews.org/$COLLECTOR/$FOLDER/ --accept-regex .*index.html
		    	cat archive.routeviews.org/$COLLECTOR/$FOLDER/index.html | cut -d "\"" -f8 | grep -iv "<" | grep $YEAR-$MONTH-$DAY-16 > files.txt
				wget --execute="robots = off" --mirror --no-parent -c -N --base=http://archive.routeviews.org/$COLLECTOR/$FOLDER/ -i files.txt
		else
		        wget --execute="robots = off" --mirror --no-parent -c -N  http://archive.routeviews.org/$COLLECTOR/bgpdata/$FOLDER/RIBS/ --accept-regex .*index.html
		        cat archive.routeviews.org/$COLLECTOR/bgpdata/$FOLDER/RIBS/index.html | cut -d "\"" -f8 | grep -iv "<" | grep $DATE.16 > files.txt
				wget --execute="robots = off" --mirror --no-parent -c -N --base=http://archive.routeviews.org/$COLLECTOR/bgpdata/$FOLDER/RIBS/ -i files.txt
		fi 	
	done
done
