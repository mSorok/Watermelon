import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import Utils from "../../Utils";

const React = require("react");


export default class Representations extends React.Component {
    render() {
        const naturalProduct = this.props.naturalProduct;
        let name = "no name available";
        if(naturalProduct.compoundName != null && naturalProduct.compoundName != "" ) {

            name = Utils.capitalize(naturalProduct.compoundName.replace(/_/g, " "));
        }

        return (
            <Card className="compoundCardItem">
                <Card.Body>
                    <Card.Title className="text-primary">Representations</Card.Title>
                    <br />
                    <Table responsive bordered hover size="sm" >
                        <tbody>
                        <tr key={"represent_id"}>
                            <td>AFC id</td>
                            <td>{naturalProduct.afc_id}</td>
                        </tr>
                        <tr key={"represent_name"}>
                            <td>Name</td>
                            <td>{name}</td>
                        </tr>
                        <tr key={"represent_iupac"}>
                            <td>IUPAC name</td>
                            <td>{naturalProduct.iupacName}</td>
                        </tr>
                        <tr  key={"represent_inchi"}>
                            <td>InChI</td>
                            <td>{naturalProduct.inchi}</td>
                        </tr>
                        <tr key={"represent_inchik"}>
                            <td>InChIKey</td>
                            <td>{naturalProduct.inchikey}</td>
                        </tr>
                        <tr  key={"represent_csmiles"}>
                            <td>Canonical SMILES (CDK)</td>
                            <td>{naturalProduct.unique_smiles || naturalProduct.original_smiles }</td>
                        </tr>
                        <tr key={"represent_dsmiles"}>
                            <td>Deep SMILES</td>
                            <td>{naturalProduct.deep_smiles || "could not be computed"}</td>
                        </tr>
                        <tr key={"represent_mf"}>
                            <td>Murcko Framework</td>
                            <td>{naturalProduct.murko_framework || "not applicable"}</td>
                        </tr>


                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        );
    }
}