screen -X -S MessageQueue kill
screen -X -S Collector kill
screen -X -S DataProcessor kill
screen -X -S DataAPI kill
screen -X -S ConfigAPI kill
screen -X -S ConfigJavaAPI kill
screen -X -S cepEngine kill
screen -X -S virtualSensorProcessing kill

echo 'Killing previous instances ...'
sleep 3
screen -S MessageQueue -d -m ./message-queue-script.sh
echo 'Starting Message Queue ...'
sleep 10
screen -S virtualSensorProcessing -d -m ./virtual-sensor-processing-script.sh
echo 'Starting virtual Sensor Processing  ...'
sleep 5
screen -S cepEngine -d -m ./cep-engine-script.sh
echo 'Starting CEP Engine ...'
sleep 20
screen -S Collector -d -m ./collector-script.sh
echo 'Starting Collector ...'
screen -S DataProcessor -d -m ./data-processor-script.sh
echo 'Starting Messaging Processor ...'
screen -S DataAPI -d -m ./data-api-script.sh
echo 'Starting Data API ...'
screen -S ConfigAPI -d -m ./configAPI-script.sh
screen -S ConfigJavaAPI -d -m ./config-script.sh
echo 'Starting Config API ...'
sleep 5
