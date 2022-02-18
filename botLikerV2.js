function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
}

async function start() {
    for (let i = 0; i <= 1000; i++) {
        console.log('Ciclo '+i)
        var min = 8,
            max = 20;
        var rand = Math.floor(Math.random() * (max - min + 1) + min);

        console.log('Espera ' + (1000 * rand) + ' ms');
        await delay(1000 * rand);
        if ((document.querySelector(".fr66n>button>div>span>svg").getAttribute('aria-label')) === 'Me gusta') {
            ele = document.querySelector(".fr66n>button")
            ele.click();
            console.log('Like: ' + window.location);
            if (ele = document.querySelector(".l8mY4>button")) ele.click();
        }else{
            console.log('Siguiente ya tiene like: ' + window.location);
            if (ele = document.querySelector(".l8mY4>button")) ele.click();
        }

    }
}

start();
