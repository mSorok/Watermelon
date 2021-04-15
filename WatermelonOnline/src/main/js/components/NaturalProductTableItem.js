import Image from "react-bootstrap/Image";
import {LinkContainer} from "react-router-bootstrap";
import Utils from "../Utils";

const React = require("react");


export default class NaturalProductTableItem extends React.Component {
    render() {
        const linkToCompoundPage = "/compound/afc_id/" + this.props.naturalProduct.afc_id;
        const structure = Utils.drawMoleculeBySmiles(this.props.naturalProduct.unique_smiles);

        let name = "no name available";
        if(this.props.naturalProduct.compoundName != null && this.props.naturalProduct.compoundName != "" ) {

            name = Utils.capitalize(this.props.naturalProduct.compoundName.replace(/_/g, " "));
        }

        return (
            <LinkContainer to={linkToCompoundPage}>
                <tr>
                    <td><Image src={structure.toDataURL()} alt="🥥" fluid/></td>
                    <td>{this.props.naturalProduct.afc_id}</td>
                    <td>{name}</td>
                    <td>{this.props.naturalProduct.molecular_formula || this.props.naturalProduct.molecularFormula}</td>
                    <td>{Math.round((this.props.naturalProduct.molecular_weight + Number.EPSILON)  * 100) / 100}</td>
                    <td>{this.props.naturalProduct.inchi}</td>
                </tr>
            </LinkContainer>
        );
    }
}