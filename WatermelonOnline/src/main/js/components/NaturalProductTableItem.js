import Image from "react-bootstrap/Image";
import {LinkContainer} from "react-router-bootstrap";
import Utils from "../Utils";

const React = require("react");


export default class NaturalProductTableItem extends React.Component {
    render() {
        const linkToCompoundPage = "/compound/coconut_id/" + this.props.naturalProduct.afc_id;
        const structure = Utils.drawMoleculeBySmiles(this.props.naturalProduct.smiles);

        return (
            <LinkContainer to={linkToCompoundPage}>
                <tr>
                    <td><Image src={structure.toDataURL()} alt="🥥" fluid/></td>
                    <td>{this.props.naturalProduct.afc_id}</td>
                    <td>{this.props.naturalProduct.compoundName ? this.props.naturalProduct.compoundName : "no name available"}</td>
                    <td>{this.props.naturalProduct.molecular_formula || this.props.naturalProduct.molecularFormula}</td>
                    <td>{Math.round((this.props.naturalProduct.molecular_weight + Number.EPSILON)  * 100) / 100}</td>
                    <td>{this.props.naturalProduct.inchi}</td>
                </tr>
            </LinkContainer>
        );
    }
}