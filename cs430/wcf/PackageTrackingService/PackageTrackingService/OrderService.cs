using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace PackageTrackingService
{
	[ServiceContract]
	public interface IOrderService
	{

		[OperationContract]
		string GetData(int value);

		[OperationContract]
		CompositeType GetDataUsingDataContract(CompositeType composite);

		// TODO: Add your service operations here
		[OperationContract]
		string HelloWho(string name);
		[OperationContract]
		int Square(int i);
	}


	// Use a data contract as illustrated in the sample below to add composite types to service operations.
	[DataContract]
	public class CompositeType
	{
		bool boolValue = true;
		string stringValue = "Hello ";

		[DataMember]
		public bool BoolValue
		{
			get { return boolValue; }
			set { boolValue = value; }
		}

		[DataMember]
		public string StringValue
		{
			get { return stringValue; }
			set { stringValue = value; }
		}
	}
}
