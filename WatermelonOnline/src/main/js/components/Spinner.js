import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { faBahai } from "@fortawesome/free-solid-svg-icons";
const React = require("react");





export default class Spinner extends React.Component {


    //style={"color:#c10b30"}
    //<FontAwesomeIcon icon={faDharmachakra} className="standAloneIcon" size={"4x"} variant='warning' spin/>

    constructor(props) {
        super(props);
        this.size = this.props.size || "4x";
    }

    render() {
        return(
            <Container>
                <Row className="justify-content-center">
                    <FontAwesomeIcon icon={faBahai} className="standAloneIcon" size={this.size} style={{color:"#c10b30"}} spin/>
                </Row>
            </Container>
        );
    }
}