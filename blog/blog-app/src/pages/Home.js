import { useEffect } from "react";
import { Container, Row, Col } from "reactstrap";
import Base from "../components/Base";
import CategorySideMenu from "../components/CategorySideMenu";
import NewFeed from "../components/NewFeed";
import Fooster from "../components/Fooster";
import Chatbot from "../components/Chatbot";

const Home = () => {

  return (
    <Base>
      <Container className="mt-3">
        <Row>
          <Col md={2} className="pt-5">
            <CategorySideMenu />
          </Col>
          <Col md={10}>
            <NewFeed />
              <Chatbot/>
          </Col>
        </Row>
      </Container>
      <Fooster/>
    </Base>
  );
};

export default Home;
