import React, {Component} from 'react';
import {addLocaleData, IntlProvider} from 'react-intl';
import zh_CN from './locale/zh_CN';
import en_US from './locale/en_US';
import zh from 'react-intl/locale-data/zh';
import en from 'react-intl/locale-data/en';

addLocaleData([...zh, ...en]);

export default class Intl extends Component {
    constructor(props){
        super(props);
        this.state = {
            language: 'zh-CN'
        }
    }
    componentDidMount(){
        let self = this;
        window.sessionStorage.setItem('language', 'zh-CN');
        window.sessionStorage.setItem('language', navigator.language);
        window.addEventListener('setItemEvent', function(event) {
            let rlanguage;
            switch (event.newValue) {
                case "zh-CN":
                    rlanguage = 'zh-CN';
                    break;
                case "en-US":
                    rlanguage = 'en-US';
                    break;
                default:
                    rlanguage = 'zh-CN';
            }
            self.setState({
                language: rlanguage
            });
        });
        self.setState({
            language: navigator.language
        });
    }

    chooseLocale(language) {
        switch (language.split('-')[0]) {
            case 'en':
                return en_US;
            case 'zh':
                return zh_CN;
            default:
                return zh_CN;
        }
    }
    render() {
        let {locale, localeMessage, children} = this.props;
        return (
            <IntlProvider
                key={locale ? locale : this.state.language}
                locale={locale ? locale : this.state.language}
                messages={localeMessage ? localeMessage : this.chooseLocale(this.state.language)}
            >
                {children}
            </IntlProvider>
        )
    }
}
