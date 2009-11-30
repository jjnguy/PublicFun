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
		// TODO: Add your service operations here
		[OperationContract]
		string HelloWho(string name);
		[OperationContract]
		int Square(int i);
	}
}
