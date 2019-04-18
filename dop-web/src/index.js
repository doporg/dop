import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import Intl from './intl/Intl'
import registerServiceWorker from './registerServiceWorker';

let originalSetItem = window.sessionStorage.setItem;
window.sessionStorage.setItem = function(key,newValue){
    let setItemEvent = new Event("setItemEvent");
    setItemEvent.newValue = newValue;
    window.dispatchEvent(setItemEvent);
    originalSetItem.apply(this,arguments);
};

ReactDOM.render(
    <Intl>
        <App />
    </Intl>
    ,
    document.getElementById('root')
);

registerServiceWorker();
