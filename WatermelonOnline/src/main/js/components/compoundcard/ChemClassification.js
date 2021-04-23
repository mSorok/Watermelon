import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Link, Redirect} from "react-router-dom";

import Button from "react-bootstrap/Button";



const React = require("react");



export default class ChemClassification extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            searchSubmitted: false,
            queryString: null,
        };
        this.handleSearchSubmit = this.handleSearchSubmit.bind(this);

    }

    handleSearchSubmit(e, value){


        this.setState(
            {
                queryString:value,
                searchSubmitted:true
            }
        );

    }




    render() {
        const naturalProduct = this.props.naturalProduct;

        let api_url = "/search/chemclass?query=";




        let classTable = [];

        if (naturalProduct.chemicalTaxonomyNPclassifierClass == "" && naturalProduct.chemicalTaxonomyNPclassifierSuperclass == "" && naturalProduct.chemicalTaxonomyNPclassifierPathway=="") {

            return(
                <Card className="compoundCardItem">
                    <Card.Body>
                        <Card.Title className="text-primary">Chemical classification</Card.Title>
                        <Card.Subtitle size="sm"><FontAwesomeIcon icon="info" fixedWidth/>Computed with <a target="_blank" rel="noopener noreferrer"  href="https://npclassifier.ucsd.edu/">NP Classifier</a></Card.Subtitle>
                        <br />
                        <p>No chemical classification seems to exist for this compound</p>
                    </Card.Body>
                </Card>
            );
        } else {

            //TODO links to the API to retrieve all with this classification

            if (naturalProduct.chemicalTaxonomyNPclassifierPathway != "" &&  naturalProduct.chemicalTaxonomyNPclassifierPathway != "NaN" ) {

                if(naturalProduct.chemicalTaxonomyNPclassifierPathway.includes(";")){
                    let longer_line = "";
                    let tcc = naturalProduct.chemicalTaxonomyNPclassifierPathway.split("; ");
                    for(let z=0; z<tcc.length; z++){
                        longer_line+=<Button style={{fontSize: 12}} size="xs" variant="link"
                                             onClick={(e) => this.handleSearchSubmit(e, tcc[z])}>{tcc[z]}&nbsp;
                            <FontAwesomeIcon icon="search-plus" fixedWidth/></Button>;
                    }

                    classTable.push(
                        <tr key={"sc_cpath"}>
                            <td>Pathway</td>

                            <td>{longer_line}</td>
                        </tr>
                    );



                }else {

                    classTable.push(
                        <tr key={"sc_cpath"}>
                            <td>Pathway</td>

                            <td><Button style={{fontSize: 12}} size="xs" variant="link"
                                        onClick={(e) => this.handleSearchSubmit(e, naturalProduct.chemicalTaxonomyNPclassifierPathway)}>{naturalProduct.chemicalTaxonomyNPclassifierPathway}&nbsp;
                                <FontAwesomeIcon icon="search-plus" fixedWidth/></Button></td>
                        </tr>
                    );
                }
            } else {
                classTable.push(
                    <tr key={"sc_cpath"}>
                        <td>Pathway</td>
                        <td>No known pathway</td>
                    </tr>
                );
            }

            if (naturalProduct.chemicalTaxonomyNPclassifierSuperclass != "" && naturalProduct.chemicalTaxonomyNPclassifierSuperclass != "NaN" ) {


                    if(naturalProduct.chemicalTaxonomyNPclassifierSuperclass.includes(";")){
                        let longer_line = "";
                        let tcc = naturalProduct.chemicalTaxonomyNPclassifierSuperclass.split("; ");
                        for(let z=0; z<tcc.length; z++){
                            longer_line+=<Button style={{fontSize: 12}} size="xs" variant="link"
                                                 onClick={(e) => this.handleSearchSubmit(e, tcc[z])}>{tcc[z]}&nbsp;
                                <FontAwesomeIcon icon="search-plus" fixedWidth/></Button>;
                        }

                        classTable.push(
                            <tr key={"sc_sclass"}>
                                <td>Superclass</td>

                                <td>{longer_line}</td>
                            </tr>
                        );



                    }else {

                        classTable.push(
                            <tr key={"cc_sclass"}>
                                <td>Superclass</td>
                                <td><Button style={{fontSize: 12}} size="xs" variant="link"
                                            onClick={(e) => this.handleSearchSubmit(e, naturalProduct.chemicalTaxonomyNPclassifierSuperclass)}>{naturalProduct.chemicalTaxonomyNPclassifierSuperclass}&nbsp;
                                    <FontAwesomeIcon icon="search-plus" fixedWidth/></Button></td>
                            </tr>
                        );
                    }
            } else {
                classTable.push(
                    <tr key={"cc_sclass"}>
                        <td>Superclass</td>
                        <td>No known superclass</td>
                    </tr>
                );
            }

            if (naturalProduct.chemicalTaxonomyNPclassifierClass != "" && naturalProduct.chemicalTaxonomyNPclassifierClass != "NaN") {


                    if (naturalProduct.chemicalTaxonomyNPclassifierClass.includes(";")) {
                        let longer_line = "";
                        let tcc = naturalProduct.chemicalTaxonomyNPclassifierClass.split("; ");
                        for (let z = 0; z < tcc.length; z++) {
                            longer_line += <Button style={{fontSize: 12}} size="xs" variant="link"
                                                   onClick={(e) => this.handleSearchSubmit(e, tcc[z])}>{tcc[z]}&nbsp;
                                <FontAwesomeIcon icon="search-plus" fixedWidth/></Button>;
                        }

                        classTable.push(
                            <tr key={"sc_cclass"}>
                                <td>Class</td>

                                <td>{longer_line}</td>
                            </tr>
                        );


                    } else {

                    classTable.push(
                        <tr key={"sbc_cclass"}>
                            <td>Class</td>
                            <td><Button style={{fontSize: 12}} size="xs" variant="link"
                                        onClick={(e) => this.handleSearchSubmit(e, naturalProduct.chemicalTaxonomyNPclassifierClass)}>{naturalProduct.chemicalTaxonomyNPclassifierClass}&nbsp;
                                <FontAwesomeIcon icon="search-plus" fixedWidth/></Button></td>
                        </tr>
                    );
                }
            } else {
                classTable.push(
                    <tr key={"sbc_cclass"}>
                        <td>Class</td>
                        <td>No known class</td>
                    </tr>
                );
            }





            return (
                <Card className="compoundCardItem">
                    <Card.Body>
                        <Card.Title className="text-primary">Chemical classification</Card.Title>
                        <Card.Subtitle size="xs"><FontAwesomeIcon icon="info" fixedWidth/>Computed with <a target="_blank" rel="noopener noreferrer"  href="https://npclassifier.ucsd.edu/">NP Classifier</a></Card.Subtitle>
                        <br />
                        <Table size="sm">
                            <tbody>
                            {classTable}

                            </tbody>
                        </Table>
                    </Card.Body>
                    {this.state.searchSubmitted && <Redirect to={"/search/chemclass/" + encodeURIComponent(this.state.queryString)}/> }
                </Card>


            );
        }


    }
}