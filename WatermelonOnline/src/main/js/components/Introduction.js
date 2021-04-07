import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Toast from "react-bootstrap/Toast";
import Alert from "react-bootstrap/Alert";
import {useState} from "react";


const React = require("react");


export default class Introduction extends React.Component {





    render(){
        return (
            <Container>
                <Row>
                    <p style={{textAlign: "justify"}}>Natural Products Online is an open source project for Natural Products (NPs) storage, search and analysis.
                        The present version hosts Watermelon Molecules Online an open project for the exploration of the chemical diversity of Citrullus lanatus, the common watermelon.</p>


                </Row>
            </Container>
        );
    }
}
