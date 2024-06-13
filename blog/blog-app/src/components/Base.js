import CustomNavbar from "./CustomNavbar";
import Fooster from "./Fooster";
 import { useEffect, useState } from 'react';

const Base = ({ children}) => {



  return (
    <div className="container-fluid p-0 m-0">
      <CustomNavbar />
      {children}
    </div>
  );
};

export default Base;
