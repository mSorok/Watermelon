import Table from "react-bootstrap/Table";
import NaturalProductTableItem from "../NaturalProductTableItem";

const React = require("react");


export default class TableBrowser extends React.Component {
    render() {
        const naturalProductTableItems = this.props.watermelonMolecules.map(naturalProduct =>
            <NaturalProductTableItem key={naturalProduct.afc_id} naturalProduct={naturalProduct}/>

        );

        return (
            <Table responsive="lg" bordered hover size="sm">
                <thead>
                <tr>
                    <th className="tableThumbnail"></th>
                    <th>AFC id</th>
                    <th>Name</th>
                    <th>Mol. formula</th>
                    <th>Mol. weight</th>
                    <th>InChI</th>
                </tr>
                </thead>
                <tbody>
                    {naturalProductTableItems}
                </tbody>
            </Table>
        );
    }
}