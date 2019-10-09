<?php
//version v.b.01 - crea estadistica del log de apache sobre los accesos
//a POST /provisioning/rpki-updown HTTP/1.1. Muestra las IP's que
//accedieron y cuando lo hicieron
//Autor: Equipo UP/DOWN - RPKI - lacnic 32 - Hackathon - 2019 - Sebastian Lever Chile
$handle = fopen('rpki-demo-ssl-access_log','r') or die ("\n" . "\n" . '** Archivo no Encontrado **' . "\n" . "\n" . "\n");
$requestsCount = 0;
$cadenaIP;
$cadenaIPfecha;
$ip;
$fecha;
$ContIP = 0;
$cont = 0;
$i = 0;

while (!feof($handle)) {
    $dd = fgets($handle);
    $requestsCount++;
    $ip++;
    $parts = explode('"', $dd);
    $ip = explode("- -", $dd);
    $statusCode = substr($parts[1], 0, 170);

    if (hasRequestType($statusCode, 'POST /provisioning/rpki-updown HTTP/1.1'))
    {
    $cont++;
    $cadenaIP[$i]=$ip[0]; //Toma IP
    //FuNCIONA$fecha[$i]=$ip[1]; //Toma Fecha

    $fecha[$i] = substr($ip[1], 0, 13);
    //$fecha[$i]=explode("]",$ip[1]); //Toma Fecha
    //$ip = explode("- -", $dd);
    $cadenaIPfecha[$i] = $ip[0] . substr($ip[1], 0, 13);
    $i++;


    //echo $cadenaIP;
    //echo $statusCode . " - " . "IP: " . $ip . " Fecha: " ;
    //echo "\n";
    }
}
echo "\n";
echo "*******************************************"."\n";
echo "Resumen:" . "\n";
echo "\n";
echo "Total provisioning/rpki-updown: " . $cont . "\n" . "\n";
echo "IP's:";
echo "\n";
print_r(contadorArray($cadenaIP)); //Funciona OK
//print_r(contadorArray($fecha));  //Funciona OK
echo "IP's por fecha de acceso:";
echo "\n";
print_r(contadorArray($cadenaIPfecha));


echo "\n";
echo "*******************************************"."\n";
fclose($handle);

function hasRequestType($l,$s) {
        return substr_count($l,$s) > 0;
}

function contadorArray($array)
{
	$contar=array();
	foreach($array as $value)
	{
		if(isset($contar[$value]))
		{
			$contar[$value]+=1;
		}else{
			$contar[$value]=1;
		}
	}
	return $contar;
}
?>
