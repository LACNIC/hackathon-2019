#!/bin/bash

days=(23 18 06 05 11 17 13 08 09 25 01 12)
months=(06 07 08 09 10 11 12 01 02 03 04 05)
years=(2018 2018 2018 2018 2018 2018 2018 2019 2019 2019 2019 2019)
collectors=( rrc00 rrc01 rrc02 rrc03 rrc04 rrc05 rrc06 rrc07 rrc08 rrc09 rrc10 rrc11 rrc12 rrc13 rrc14 rrc15 rrc16 rrc17 rrc18 rrc19 rrc20 rrc21 rrc22 rrc23 rrc24 )

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

		wget --execute="robots = off" --mirror --no-parent -c -N  http://data.ris.ripe.net/$COLLECTOR/$FOLDER/ --accept-regex .*index.html

		cat data.ris.ripe.net/$COLLECTOR/$FOLDER/index.html | cut -d "\"" -f8 | grep -iv "<" | grep bview.$DATE.16 > files.txt
		#mkdir -p /$COLLECTOR/$FOLDER/
		wget --execute="robots = off" --mirror --no-parent -c -N --base=http://data.ris.ripe.net/$COLLECTOR/$FOLDER/ -i files.txt
	done
done



