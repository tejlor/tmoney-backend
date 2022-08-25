#! /bin/bash

rc=0
base_dir=/srv/tmoney
log_file=$base_dir/log.txt
source_backend_dir=$base_dir/source/tmoney-backend
source_frontend_dir=$base_dir/source/tmoney-frontend
config_dir=$base_dir/config
prod_boot_dir=$base_dir/prod/boot
prod_www_dir=$base_dir/prod/www

function checkStatus {
    if [[ $rc -eq 0 ]] ; then
    printf " OK\n"
    else
    printf " Błąd\n"
    exit
    fi
}

function goToBackend {
    printf "Przechodzę do katalogu backend... "
    cd $source_backend_dir >> $log_file 2>&1
    rc=$?
    checkStatus
}


function goToFrontend {
    printf "Przechodzę do katalogu frontend... "
    cd $source_frontend_dir >> $log_file 2>&1
    rc=$?
    checkStatus
}

function pull {
    printf "Pobieram źródła... "
    git checkout master >> $log_file 2>&1
    git pull --ff-only >> $log_file 2>&1
    rc=$?
    checkStatus
}

function copyBackendConfig {
    printf "Kopiuję konfigurację... "
    cp $config_dir/application-prod.properties $source_backend_dir/src/main/resources/application-prod.properties
    rc=$?
    checkStatus
}

function copyFrontendConfig {
    printf "Kopiuję konfigurację... "
    cp $config_dir/environment.prod.ts $source_frontend_dir/src/environments/environment.prod.ts
    rc=$?
    checkStatus
}

function buildBackend {
    printf "Buduję backend... "
    mvn clean package -Dmaven.test.skip=true >> $log_file 2>&1
    rc=$?
    checkStatus
}

function buildFrontend {
    printf "Pobieram pakiety npm... "
    npm install >> $log_file 2>&1
    rc=$?
    checkStatus
    printf "Buduję frontend... "
    npm run build >> $log_file 2>&1
    rc=$?
    checkStatus
}

function copyBackend {
    printf "Kopiuję backend na produkcję... "
    cp target/tmoney.jar $prod_boot_dir/tmoney.jar >> $log_file 2>&1
    rc=$?
    checkStatus
}

function copyFrontend {
    printf "Kopiuję frontend na produkcję... "
    sudo find /srv/tmoney/prod/www/* ! -name ".htaccess" -delete >> $log_file 2>&1
    sudo cp -rp dist/tmoney/. $prod_www_dir/ >> $log_file 2>&1
    sudo chown -R www-data:www-data $prod_www_dir 2>&1
    rc=$?
    checkStatus
}

function goToBoot {
    cd $prod_boot_dir
}

function restartBackend {
    printf "Zatrzymuję spring-boot... "
    ./stop.sh
    printf "Uruchamiam spring-boot... "
    ./start.sh
}

printf "========================== $(date +'%F %R') ===============================\n" >> $log_file
goToBackend
pull
copyBackendConfig
buildBackend
copyBackend
goToFrontend
pull
copyFrontendConfig
buildFrontend
copyFrontend
goToBoot
restartBackend
printf "\n\n\n" >> $log_file
read -p "Zakończono"
