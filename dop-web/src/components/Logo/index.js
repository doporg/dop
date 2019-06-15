import React, {PureComponent} from 'react';
import {Link} from 'react-router-dom';
import {injectIntl, FormattedMessage} from 'react-intl';

export default class Logo extends PureComponent {
    render() {
        return (
            <Link to="/" style={{...styles.logoStyle, ...this.props.style}}>
                <FormattedMessage
                    id="base.title"
                    defaultMessage="南京大学DevOps学生实训平台"
                />
            </Link>
        );
    }
}

const styles = {
    logoStyle: {
        display: 'block',
        maxWidth: '360px',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap',
        fontSize: '20px',
        color: '#fff',
        fontWeight: 'bold',
        textDecoration: 'none',
    },
};
