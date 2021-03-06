import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import Utils from "../Utils";

const React = require("react");


//This is the small cards that appear on the home page - need to find what catches the search

export default class NaturalProductCardItem extends React.Component {
    render() {
        const linkToCompoundPage = "/compound/afc_id/" + this.props.naturalProduct.afc_id;
        const structure = Utils.drawMoleculeBySmiles(this.props.naturalProduct.unique_smiles);

        //TODO either here (test for Tanimoto field), either create a Tanimoto card

        var cardTitle = "";

        if(this.props.naturalProduct.hasOwnProperty('tanimoto') && this.props.naturalProduct.tanimoto &&  this.props.naturalProduct.tanimoto>0 ){

            var tanomotoScore =  Math.round((this.props.naturalProduct.tanimoto*100 + Number.EPSILON) * 100)/100;


            cardTitle = <>
                <Card.Link href={linkToCompoundPage} className="cardItemHeadline">{this.props.naturalProduct.afc_id}&nbsp;&nbsp;</Card.Link>
                <p style={{color: "#FC6B1E"}}>{tanomotoScore} %</p>
            </>;
        }else{
            cardTitle = <Card.Link href={linkToCompoundPage} className="cardItemHeadline">{this.props.naturalProduct.afc_id}</Card.Link>;
        }

        let name = "no name available";
        if(this.props.naturalProduct.compoundName != null && this.props.naturalProduct.compoundName != "" ) {

            name = Utils.capitalize(this.props.naturalProduct.compoundName.replace(/_/g, " "));
        }


        return (
            <Card className="cardBrowserItem">
                <Card.Link href={linkToCompoundPage} className="cardItemImg"><Card.Img variant="top" src={structure.toDataURL()} alt="🥥"/></Card.Link>
                <Card.Body>
                    <Card.Title>
                        {cardTitle}
                    </Card.Title>
                    <Card.Subtitle>{name}</Card.Subtitle>
                    <Table>
                        <tbody>
                        <tr>
                            <td>Mol. formula</td>
                            <td>{this.props.naturalProduct.molecular_formula || this.props.naturalProduct.molecularFormula}</td>
                        </tr>
                        <tr>
                            <td>Mol. weight</td>
                            <td>{ Math.round((this.props.naturalProduct.molecular_weight + Number.EPSILON)  * 100) / 100 }</td>
                        </tr>

                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        );
    }
}