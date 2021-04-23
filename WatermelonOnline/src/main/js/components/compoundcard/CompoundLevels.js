import Card from "react-bootstrap/Card";
import Table from "react-bootstrap/Table";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Tooltip from "react-bootstrap/Tooltip";
import Utils from "../../Utils";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";

const React = require("react");


export default class CompoundLevels extends React.Component {

    createRowWithOverlayTooltip(name, value, tooltiptext) {
        return (
            <tr>
                <OverlayTrigger key={name + "Overlay"} placement="top" overlay={
                    <Tooltip id={name + "Tooltip"}>{tooltiptext}</Tooltip>
                }>
                    <td>{Utils.capitalize(name)} <FontAwesomeIcon icon="question-circle" fixedWidth/></td>
                </OverlayTrigger>
                <td>{value}</td>
            </tr>
        );
    }

    render() {

        let plant_parts = {
            F: "flesh red flesh",
            G:"seedling",
            H:"heart tissue",
            J: "juice",
            L:"leaf",
            P:"peel",
            R:"rind",
            S:"seed",
            T:"root",
            U:"detected but undeclared",
            V:"plant",
            W:"powdered juice",
            Y:"yellow flesh"
        };

        const naturalProduct = this.props.naturalProduct;

        let full_table = [];
        let header = [];
        if(naturalProduct.compoundLevels.length>0){

            let theader = [];

            theader.push(
                <th id={"head_cl_pp"}>
                    Plant part
                </th>
            );

            theader.push(
                <th id={"head_cl_min"}>
                    Min. levels
                </th>
            );

            theader.push(
                <th id={"head_cl_max"}>
                    Max. levels
                </th>
            );

            theader.push(
                <th id={"head_cl_pm"}>
                    +/-
                </th>
            );

            theader.push(
                <th id={"head_cl_unit"}>
                    Unit
                </th>
            );

            theader.push(
                <th id={"head_cl_source"}>
                    Source
                </th>
            );

            theader.push(
                <th id={"head_cl_comment"}>
                    Comment
                </th>
            );

            header.push(
                <thead>
                <tr>
                    {theader}
                </tr>
                </thead>

            );
            //console.log(naturalProduct.compoundLevels);
            let table_body = [];
            for(let line in naturalProduct.compoundLevels ){

                let compoundLevel = naturalProduct.compoundLevels[line];
                let table_body_line = [];
                let plant_parts_full = "";
                let plant_parts_array = compoundLevel["plant_part"].split(",");
                for (let i = 0; i < plant_parts_array.length; i++) {
                    plant_parts_full+=plant_parts[plant_parts_array[i]]+" ";
                }


                table_body_line.push(
                    <td>
                        {plant_parts_full}
                    </td>
                );

                table_body_line.push(
                    <td>{compoundLevel["min_value"]}</td>
                );

                table_body_line.push(
                    <td>{compoundLevel["max_value"]}</td>
                );

                table_body_line.push(
                    <td>{compoundLevel["plus_minus"]}</td>
                );

                table_body_line.push(
                    <td>{compoundLevel["unit"]}</td>
                );


                
                if(compoundLevel["source"].startsWith("PMID")){
                    table_body_line.push(
                        <td><a href={"https://pubmed.ncbi.nlm.nih.gov/"+compoundLevel["source"].split("_")[1]} target="_blank">PubMed</a></td>
                    );
                }else if(compoundLevel["source"].startsWith("phytochem")){
                    table_body_line.push(
                        <td><a href={"phytochem.nal.usda"} target="_blank">Phytochem@USDA</a></td>
                    );
                }else if(compoundLevel["source"].startsWith("CuCyc")){
                    table_body_line.push(
                        <td><a href={"http://cucyc.feilab.net/"} target="_blank">CuCyc</a></td>
                    );
                }else{
                    table_body_line.push(
                        <td>{compoundLevel["source"]}</td>
                    );
                }


                table_body_line.push(
                    <td>{compoundLevel["comment"]}</td>
                );




                table_body.push(
                    <tr>
                        {table_body_line}
                    </tr>

                );

            }


            full_table.push(
                <Table striped bordered hover id={"compound_levels"}>
                    {header}
                    <tbody>
                    {table_body}
                    </tbody>
                </Table>
            );

        }else{


            return(
                <Card className="compoundCardItem">
                    <Card.Body>
                        <Card.Title className="text-primary">Known Compound Levels</Card.Title>
                        <br />


                        <p>Compound levels unknown in any plant part</p>

                    </Card.Body>
                </Card>
            );
        }



        return (
            <Card className="compoundCardItem">
                <Card.Body>
                    <Card.Title className="text-primary">Known Compound Levels</Card.Title>
                    <br />

                    {full_table}

                </Card.Body>
            </Card>
        );
    }

}