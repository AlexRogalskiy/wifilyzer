@Echo Off
start javaw -Xms1024m -Xmx1024m -jar target/wifilyzer-1.0.0-jar-with-dependencies.jar -bssid 46:0a64:b1:df:51 -input-source src/main/resources/input.txt -output-source src/main/resources/output.txt