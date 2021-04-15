import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Image from "react-bootstrap/Image";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Table from "react-bootstrap/Table";
import Button from "react-bootstrap/Button";
import Utils from "../../Utils";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";


const React = require("react");


export default class Overview extends React.Component {
    constructor(props) {
        super(props);
        this.handleMolfileDownload = this.handleMolfileDownload.bind(this);
    }

    handleMolfileDownload(e, smiles, identifier) {
        e.preventDefault();

        const download = document.createElement("a");

        download.setAttribute("href", "data:chemical/x-mdl-molfile;charset=utf-8," + encodeURIComponent(Utils.getMolfileStringBySmiles(smiles)));
        download.setAttribute("download", "Molfile_V3_" + identifier + ".mol");
        download.style.display = "none";

        document.body.appendChild(download);
        download.click();
        document.body.removeChild(download);
    }

    render() {
        const naturalProduct = this.props.naturalProduct;
        const structure = Utils.drawMoleculeBySmiles(naturalProduct.unique_smiles);

        let cas_registry_num = "";
        if(naturalProduct.cas != null && naturalProduct.cas != ""){
            cas_registry_num = <tr><td>CAS registry number</td><td>{naturalProduct.cas}</td></tr>;
        }else{
            cas_registry_num = <tr><td>CAS registry number</td><td>-</td></tr>;
        }

        let starsAnnotation = [];



        let name = "no name available";
        if(naturalProduct.compoundName != null && naturalProduct.compoundName != "" ) {

            name = Utils.capitalize(naturalProduct.compoundName.replace(/_/g, " "));
        }



        return (
            <Card className="compoundCardItem">
                <Card.Body>
                    <Card.Title className="text-primary">{naturalProduct.afc_id}</Card.Title>
                    <br />
                    <Row>
                        <Col sm={4}>
                            <Image src={structure.toDataURL()} alt={<FontAwesomeIcon icon="file-image" className="standAloneIcon" size="3x"/>} fluid/>
                        </Col>
                        <Col sm={8}>
                            <Table size="sm">
                                <tbody>
                                <tr>
                                    <td>Name</td>
                                    <td>{name}</td>
                                </tr>
                                <tr>
                                    <td>Mol. formula</td>
                                    <td>{naturalProduct.molecularFormula || naturalProduct.molecular_formula}</td>
                                </tr>
                                {cas_registry_num}
                                <tr>
                                    <td>Mol. weight</td>
                                    <td>{ Math.round((naturalProduct.molecular_weight + Number.EPSILON) * 10000) / 10000 || Math.round((naturalProduct.molecularWeight + Number.EPSILON) * 10000) / 10000}</td>
                                </tr>

                                </tbody>
                            </Table>
                            <Button id="downloadMolfile" variant="outline-primary" size="sm" onClick={(e) => this.handleMolfileDownload(e, naturalProduct.unique_smiles, naturalProduct.afc_id)}>
                                <FontAwesomeIcon icon="file-download" fixedWidth/>
                                &nbsp;Molfile
                            </Button>
                        </Col>
                    </Row>
                </Card.Body>
            </Card>
        );
    }
}