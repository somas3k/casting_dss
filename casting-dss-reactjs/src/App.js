import React from "react";
import {Tab, Tabs} from "react-bootstrap";
import {Form, Field} from 'react-final-form'

const Header = () => {
    return (
        <>
        </>
    );
}
const ConfigurationComponent = () => {
    return (
        <div className="p-3">
            <CostQualityFactorFormComponent/>
            <ThicknessFormComponent/>
            <NormFormComponent/>
        </div>
    )
}

const NormFormComponent = ({formId}) => {
    return (
        <div className="mb-3">
            <label htmlFor={formId} className="form-label">Norm</label>
            <select className="form-select">
                <option>GJS_800_10</option>
                <option>GJS_900_8</option>
                <option>GJS_1050_6</option>
                <option>GJS_1200_3</option>
                <option>GJS_1400_1</option>
            </select>
        </div>
    )
}

const ThicknessFormComponent = () => {
    return (
        <div className="mb-3">
            <label className="form-label">Thickness</label>
            <Field name="thickness" type="text" component="input" className="form-control"/>
        </div>
    )
}

const CostQualityFactorFormComponent = () => {
    return (
        <Field name="factor">
            {props => (
                <div className="mb-3">
                    <label>Cost/Quality Factor</label>
                    <div className="container">
                        <div className="row" style={{display: "flex", alignItems: "center"}}>
                            <div className="col" style={{display: "flex", justifyContent: "flex-end"}}>
                                <label>Cost</label>
                            </div>
                            <div className="col" style={{display: "flex", alignItems: "center"}}>
                                <input type="range" className="form-range" name={props.input.name} value={props.input.value}
                                       onChange={props.input.onChange}/>
                            </div>
                            <div className="col">
                                <label>Quality</label>
                            </div>
                        </div>
                        <div className="row" style={{textAlign: "center"}}>
                            <label>{props.input.value}</label>
                        </div>
                    </div>
                </div>
            )}
        </Field>
    );
}

function NormsComponent() {
    return null;
}

const CostFuncParamsComponent = () => {
    return (
        <div className="p-3">

        </div>
    );
}

function ProdRangesComponent() {
    return null;
}

function AlgoRunnerComponent() {
    return null;
}

const onSubmit = async values => {

}

const Body = () => {
    return (
        <Form
            onSubmit={onSubmit}
            initialValues={{ factor: 50 }}
            render={({handleSubmit, form, submitting, pristine, values}) => (
                <form onSubmit={handleSubmit}>
                    <Tabs defaultActiveKey="config" id="tabs" className="mb-5">
                        <Tab eventKey="config" title="Configuration">
                            <ConfigurationComponent/>
                        </Tab>
                        <Tab eventKey="norms" title="Norms">
                            <NormsComponent/>
                        </Tab>
                        <Tab eventKey="cost-func-params" title="Cost function parameters">
                            <CostFuncParamsComponent/>
                        </Tab>
                        <Tab eventKey="prod-ranges" title="Production ranges">
                            <ProdRangesComponent/>
                        </Tab>
                        <Tab eventKey="algo-runner" title="Algorithms runner">
                            <AlgoRunnerComponent/>
                        </Tab>
                    </Tabs>
                </form>
            )}>
        </Form>
    )
}

const Footer = () => {
    return (
        <>
        </>
    );
}

const App = () => {
    return (
        <>
            <Header/>
            <Body/>
            <Footer/>
        </>
    )
};

export default App;
