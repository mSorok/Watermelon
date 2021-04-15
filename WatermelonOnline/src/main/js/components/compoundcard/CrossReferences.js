import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import Error from "../Error";
import Spinner from "../Spinner";
import Button from "react-bootstrap/Button";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

const React = require("react");
const restClient = require("../../restClient");


export default class CrossReferences extends React.Component {



    render() {

        const naturalProduct = this.props.naturalProduct;

        let chebilink = "https://www.ebi.ac.uk/chebi/searchId.do?chebiId=";
        let kegglink = "https://www.genome.jp/dbget-bin/www_bget?";
        let lipidmapslink = "https://www.lipidmaps.org/data/LMSDRecord.php?LMID=";
        let hmdblink = "https://hmdb.ca/metabolites/";
        let pubchemlink = "https://pubchem.ncbi.nlm.nih.gov/compound/";
        let foodblink = "https://foodb.ca/compounds/";

        let kegg = naturalProduct.kegg;
        let lipidmaps = naturalProduct.lipidmaps;
        let hmdb = naturalProduct.hmdb;
        let pubchem = naturalProduct.pubchem;
        let chebi = naturalProduct.chebi;
        let foodb = naturalProduct.foodb;


        let linksToSources = [];

        if (kegg != null && kegg != "") {
            const buttonToSource =
                <Button id={"linkTo_kegg"} variant="outline-primary" size="sm" href={kegglink + kegg} target="_blank">
                    <FontAwesomeIcon icon="external-link-alt" fixedWidth/>
                </Button>;

            linksToSources.push(
                <tr key={"ref_kegg"}>
                    <td>KEGG</td>
                    <td>{buttonToSource}</td>
                </tr>
            );
        }

        if (chebi != null && chebi != "") {
            const buttonToSource =
                <Button id={"linkTo_chebi"} variant="outline-primary" size="sm" href={chebilink + chebi} target="_blank">
                    <FontAwesomeIcon icon="external-link-alt" fixedWidth/>
                </Button>;

            linksToSources.push(
                <tr key={"ref_chebi"}>
                    <td>ChEBI</td>
                    <td>{buttonToSource}</td>
                </tr>
            );
        }

        if (lipidmaps != null && lipidmaps != "") {
            const buttonToSource =
                <Button id={"linkTo_lipidmaps"} variant="outline-primary" size="sm" href={lipidmapslink + lipidmaps} target="_blank">
                    <FontAwesomeIcon icon="external-link-alt" fixedWidth/>
                </Button>;

            linksToSources.push(
                <tr key={"ref_lipidmaps"}>
                    <td>LipidMaps</td>
                    <td>{buttonToSource}</td>
                </tr>
            );
        }

        if (pubchem != null && pubchem != "") {
            const buttonToSource =
                <Button id={"linkTo_pubchem"} variant="outline-primary" size="sm" href={pubchemlink + pubchem} target="_blank">
                    <FontAwesomeIcon icon="external-link-alt" fixedWidth/>
                </Button>;

            linksToSources.push(
                <tr key={"ref_pubchem"}>
                    <td>PubChem</td>
                    <td>{buttonToSource}</td>
                </tr>
            );
        }

        if (hmdb != null && hmdb != "") {
            const buttonToSource =
                <Button id={"linkTo_hmdb"} variant="outline-primary" size="sm" href={hmdblink + hmdb} target="_blank">
                    <FontAwesomeIcon icon="external-link-alt" fixedWidth/>
                </Button>;

            linksToSources.push(
                <tr key={"ref_hmdb"}>
                    <td>HMDB</td>
                    <td>{buttonToSource}</td>
                </tr>
            );
        }

        if (foodb != null && foodb != "") {
            const buttonToSource =
                <Button id={"linkTo_foodb"} variant="outline-primary" size="sm" href={foodblink + foodb} target="_blank">
                    <FontAwesomeIcon icon="external-link-alt" fixedWidth/>
                </Button>;

            linksToSources.push(
                <tr key={"ref_foodb"}>
                    <td>FooDB</td>
                    <td>{buttonToSource}</td>
                </tr>
            );
        }


        if (linksToSources.length == 0) {
            return (

                <Card className="compoundCardItem">
                    <Card.Body>
                        <Card.Title className="text-primary">Cross References</Card.Title>
                        <br/>

                        <p>No know cross-references</p>
                    </Card.Body>
                </Card>
            );
        } else {



        return (
            <Card className="compoundCardItem">
                <Card.Body>
                    <Card.Title className="text-primary">Cross References</Card.Title>
                    <br/>
                    <Table size="sm">
                        <thead>
                        <tr>
                            <th>Database</th>
                            <th>External link</th>
                        </tr>
                        </thead>
                        <tbody>
                        {linksToSources}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        );
    }
    }
}


/*

const { error, isLoaded, sourceNaturalProducts } = this.state;

constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            sourceNaturalProduct: []
        };
    }


componentDidMount() {
        this.fetchSourcesByInchikey(this.props.naturalProduct.inchikey);
    }

    fetchSourcesByInchikey(inchikey) {
        restClient({
            method: "GET",
            path: "/api/source/search/findBySimpleInchiKey?inchikey=" + encodeURIComponent(inchikey)
        }).then(
            (response) => {
                this.setState({
                    isLoaded: true,
                    sourceNaturalProducts: response.entity._embedded.sourceNaturalProducts
                });
            },
            (error) => {
                this.setState({
                    isLoaded: true,
                    error: error
                });
            });
    }


 */