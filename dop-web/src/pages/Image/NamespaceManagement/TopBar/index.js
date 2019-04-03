import React, {PureComponent} from 'react';
import {Button} from '@icedesign/base';
import PropTypes from 'prop-types';

export default class TopBar extends PureComponent {

    constructor(props) {
        super(props);
    }

    render() {
        const {title, buttonText, extraBefore,extraDelete, extraAfter, style} = this.props;

        return (
            <div style={{...styles.container, ...style}}>
                {extraBefore || <div style={styles.title}>{title || ''}</div>}
                {extraDelete ||(
                    <div style={styles.buttons}>
                        {buttonText ? (
                            <Button size="large" type="primary" warning>删除</Button>
                        ):null}
                    </div>
                )}
                {extraAfter || (
                    <div style={styles.buttons}>
                        {buttonText ? (
                            <Button size="large" type="primary">
                                {buttonText}
                            </Button>
                        ) : null}
                    </div>
                )}
            </div>
        );
    }
}

TopBar.propTypes = {
    extraBefore: PropTypes.element,
    extraAfter: PropTypes.element,
    extraDelete: PropTypes.element,
    style: PropTypes.object,
};

TopBar.defaultProps = {
    extraAfter: null,
    extraBefore: null,
    extraDelete: null,
    style: {},
};

const styles = {
    container: {
        position: 'fixed',
        top: '62px',
        left: '240px',
        right: '0px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: '60px',
        padding: '0 20px',
        zIndex: '99',
        background: '#fff',
        boxShadow: 'rgba(0, 0, 0, 0.2) 2px 0px 4px',
    },
};
